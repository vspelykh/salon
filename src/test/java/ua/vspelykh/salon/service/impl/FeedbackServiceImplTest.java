package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dto.FeedbackDto;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.FeedbackService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.*;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestFeedbackDto;

class FeedbackServiceImplTest extends AbstractServiceTest {

    @Mock
    private FeedbackDao feedbackDao;

    @Mock
    private AppointmentDao appointmentDao;

    @Mock
    private UserDao userDao;

    @InjectMocks
    FeedbackService feedbackService = new FeedbackServiceImpl();

    @Test
    void saveNew() throws ServiceException, TransactionException, DaoException {
        Feedback testFeedback = getTestFeedback();
        testFeedback.setId(null);
        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(feedbackDao).create(testFeedback);
        doNothing().when(transaction).commit();
        feedbackService.save(testFeedback);
        verifyTransactionStart();
        verify(feedbackDao, times(1)).create(testFeedback);
        verifyTransactionCommit();
    }

    @Test
    void saveExist() {
        assertThrows(ServiceException.class, () -> feedbackService.save(getTestFeedback()));
    }

    @Test
    void getFeedbacksByMasterId() throws DaoException, ServiceException, TransactionException {
        User client = getTestUser();
        client.setId(2);

        when(feedbackDao.getFeedbacksByMasterId(ID_VALUE, 1)).thenReturn(List.of(getTestFeedback()));
        when(appointmentDao.findById(ID_VALUE)).thenReturn(getTestAppointment());
        when(userDao.findById(2)).thenReturn(client);
        List<FeedbackDto> dtos = feedbackService.getFeedbacksByMasterId(ID_VALUE, 1);

        assertEquals(getTestFeedbackDto(), dtos.get(0));

        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void delete() throws ServiceException, TransactionException, DaoException {
        feedbackService.delete(ID_VALUE);
        verify(transaction).start();
        verify(feedbackDao).removeById(ID_VALUE);
        verify(transaction).commit();
    }

    @Test
    void testDeleteTransactionException() throws TransactionException {
        doThrow(TransactionException.class).when(transaction).start();

        assertThrows(ServiceException.class, () -> feedbackService.delete(ID_VALUE));
        verify(transaction).start();
        verify(transaction, never()).commit();
        verify(transaction).rollback();
    }
}