package ua.vspelykh.salon.service.impl;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.InvitationDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dao.UserLevelDao;
import ua.vspelykh.salon.model.dto.UserMasterDTO;
import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.MasterFilter;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.*;
import static ua.vspelykh.salon.service.impl.ServiceTestData.*;

class UserServiceImplTest extends AbstractServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserLevelDao userLevelDao;

    @Mock
    private FeedbackDao feedbackDao;

    @Mock
    private InvitationDao invitationDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Test
    void findById() throws DaoException, TransactionException, ServiceException {
        when(userDao.findById(ID_VALUE)).thenReturn(getTestUser());

        assertEquals(getTestUser(), userService.findById(ID_VALUE));
        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void findByEmailAndPassword() throws DaoException, TransactionException, ServiceException {
        User testUser = getTestUser();
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String encryptPassword = encryptor.encryptPassword(PASSWORD_VALUE);
        testUser.setPassword(encryptPassword);

        when(userDao.findByEmail(EMAIL_VALUE)).thenReturn(testUser);
        assertEquals(testUser, userService.findByEmailAndPassword(EMAIL_VALUE, PASSWORD_VALUE));
        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void findByEmailAndPasswordWrongPassword() throws DaoException, TransactionException {
        User testUser = getTestUser();
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String fakeEncryptPassword = encryptor.encryptPassword(anyString());
        testUser.setPassword(fakeEncryptPassword);
        when(userDao.findByEmail(EMAIL_VALUE)).thenReturn(testUser);
        assertThrows(ServiceException.class, () -> userService.findByEmailAndPassword(EMAIL_VALUE, PASSWORD_VALUE));
        verifyTransactionStart();
        verifyTransactionRollback();
    }

    @Test
    void saveNew() throws DaoException, TransactionException, ServiceException {
        when(userDao.findByEmail(EMAIL_VALUE)).thenReturn(getTestUser());
        User testUser = getTestUser();
        testUser.setId(null);

        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(userDao).create(testUser);
        doNothing().when(transaction).commit();

        userService.save(testUser);

        verifyTransactionStart();
        verify(userDao, times(1)).create(testUser);
        verifyTransactionCommit();
    }

    @Test
    void saveExist() throws DaoException, TransactionException, ServiceException {
        User testUser = getTestUser();

        doNothing().when(transaction).start();
        doNothing().when(userDao).update(testUser);
        doNothing().when(transaction).commit();

        userService.save(testUser);

        verifyTransactionStart();
        verify(userDao).update(testUser);
        verifyTransactionCommit();
    }

    @Test
    void saveThrowsException() throws DaoException, TransactionException {
        User testUser = getTestUser();
        testUser.setId(null);

        doThrow(DaoException.class).when(userDao).create(testUser);

        doNothing().when(transaction).start();
        doNothing().when(transaction).commit();

        assertThrows(ServiceException.class, () -> userService.save(testUser));

        verifyTransactionStart();
        verify(userDao, times(1)).create(testUser);
        verify(userDao, never()).findByEmail(EMAIL_VALUE);
        verifyTransactionRollback();
    }

    @Test
    void saveWithKey() throws DaoException, TransactionException, ServiceException {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String encryptPassword = encryptor.encryptPassword(KEY_VALUE);
        Invitation invitation = getTestInvitation();
        invitation.setKey(encryptPassword);
        User testUser = getTestUser();
        testUser.setId(null);

        when(invitationDao.findByEmail(EMAIL_VALUE)).thenReturn(invitation);
        when(userDao.findByEmail(EMAIL_VALUE)).thenReturn(getTestUser());


        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(userDao).create(testUser);
        doNothing().when(transaction).commit();

        userService.save(testUser, KEY_VALUE);

        verifyTransactionStart();
        verify(userDao, times(1)).create(testUser);
        verifyTransactionCommit();
    }

    @Test
    void saveWithKeyThrowsException() throws DaoException, TransactionException {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String fakeEncryptPassword = encryptor.encryptPassword(anyString());
        Invitation invitation = getTestInvitation();
        invitation.setKey(fakeEncryptPassword);
        User testUser = getTestUser();
        testUser.setId(null);

        when(invitationDao.findByEmail(EMAIL_VALUE)).thenReturn(invitation);
        when(userDao.findByEmail(EMAIL_VALUE)).thenReturn(getTestUser());

        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(userDao).create(testUser);
        doNothing().when(transaction).rollback();

        assertThrows(ServiceException.class, () -> userService.save(testUser, KEY_VALUE));

        verifyTransactionStart();
        verify(userDao, times(1)).create(testUser);
        verifyTransactionRollback();
    }

    @Test
    void getMastersDto() throws DaoException, ServiceException, TransactionException {
        when(userDao.findFiltered(any(), anyInt(), anyInt(), any()))
                .thenReturn(List.of(getTestMaster()));
        when(feedbackDao.getFeedbacksByMasterId(ID_VALUE)).thenReturn(List.of(getTestFeedback()));
        when(userLevelDao.getUserLevelByUserId(ID_VALUE)).thenReturn(getTestUserLevel());

        MasterFilter filter = createMasterFilter(List.of(), List.of(), List.of(), NAME_VALUE);
        List<UserMasterDTO> mastersDto = userService.getMastersDto(filter, DEFAULT_PAGE, DEFAULT_SIZE,
                MasterSort.NAME_ASC, UA_LOCALE);

        assertEquals(getTestUserMasterDto(), mastersDto.get(0));
        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void updateRoleAdministrator() throws TransactionException, DaoException {
        assertDoesNotThrow(() -> userService.updateRole(ID_VALUE, ADD, Role.ADMINISTRATOR));
        verifyTransactionStart();
        verify(userLevelDao, never()).isExist(anyInt());
        verify(userLevelDao, never()).create(any(UserLevel.class));
        verify(userLevelDao, never()).update(any(UserLevel.class));
        verifyTransactionCommit();
    }

    @Test
    void updateRoleNewMaster() throws TransactionException, DaoException {
        when(userLevelDao.isExist(ID_VALUE)).thenReturn(false);
        assertDoesNotThrow(() -> userService.updateRole(ID_VALUE, ADD, Role.HAIRDRESSER));
        verifyTransactionStart();
        verify(userLevelDao).isExist(anyInt());
        verify(userLevelDao).create(any(UserLevel.class));
        verify(userLevelDao, never()).update(any(UserLevel.class));
        verifyTransactionCommit();
    }

    @Test
    void updateRoleExistMaster() throws TransactionException, DaoException {
        when(userLevelDao.isExist(ID_VALUE)).thenReturn(true);
        when(userLevelDao.findById(ID_VALUE)).thenReturn(getTestUserLevel());

        assertDoesNotThrow(() -> userService.updateRole(ID_VALUE, ADD, Role.HAIRDRESSER));

        verifyTransactionStart();
        verify(userLevelDao).isExist(anyInt());
        verify(userLevelDao, never()).create(any(UserLevel.class));
        verify(userLevelDao).update(any(UserLevel.class));
        verifyTransactionCommit();
    }

    @Test
    void updateRoleRemoveMaster() throws DaoException, TransactionException {
        when(userLevelDao.isExist(ID_VALUE)).thenReturn(true);
        when(userLevelDao.findById(ID_VALUE)).thenReturn(getTestUserLevel());

        assertDoesNotThrow(() -> userService.updateRole(ID_VALUE, REMOVE, Role.HAIRDRESSER));

        verifyTransactionStart();
        verify(userLevelDao, never()).isExist(anyInt());
        verify(userLevelDao, never()).create(any(UserLevel.class));
        verify(userLevelDao).update(any(UserLevel.class));
        verifyTransactionCommit();
    }
}