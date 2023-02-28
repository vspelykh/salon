package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.dao.MasterServiceDao;
import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dto.MasterServiceDto;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.service.MasterServiceService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestBaseService;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestMasterService;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestCategory;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestMasterServiceDto;

class MasterServiceServiceImplTest extends AbstractServiceTest {

    @Mock
    private ServiceCategoryDao serviceCategoryDao;

    @Mock
    private MasterServiceDao masterServiceDao;

    @Mock
    BaseServiceDao baseServiceDao;

    @InjectMocks
    MasterServiceService masterServiceService = new MasterServiceServiceImpl();

    @Test
    void getDTOsByMasterId() throws DaoException, ServiceException, TransactionException {
        when(masterServiceDao.getAllByUserId(ID_VALUE)).thenReturn(List.of(getTestMasterService()));
        when(baseServiceDao.findById(ID_VALUE)).thenReturn(getTestBaseService());
        when(serviceCategoryDao.findById(ID_VALUE)).thenReturn(getTestCategory());

        List<MasterServiceDto> dtos = masterServiceService.getDTOsByMasterId(ID_VALUE, UA_LOCALE);

        verifyTransactionStart();
        assertEquals(1, dtos.size());
        assertEquals(getTestMasterServiceDto(), dtos.get(0));
        verifyTransactionCommit();
    }

    @Test
    void saveNew() throws ServiceException, TransactionException, DaoException {
        MasterService testMasterService = getTestMasterService();
        testMasterService.setId(null);
        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(masterServiceDao).create(testMasterService);
        doNothing().when(transaction).commit();
        masterServiceService.save(testMasterService);
        verifyTransactionStart();
        verify(masterServiceDao, times(1)).create(testMasterService);
        verifyTransactionCommit();
    }

    @Test
    void saveExist() throws ServiceException, TransactionException, DaoException {
        doNothing().when(transaction).start();
        doNothing().when(masterServiceDao).update(getTestMasterService());
        doNothing().when(transaction).commit();
        masterServiceService.save(getTestMasterService());
        verifyTransactionStart();
        verify(masterServiceDao, times(1)).update(getTestMasterService());
        verifyTransactionCommit();
    }

    @Test
    void saveThrowsException() throws DaoException, TransactionException {
        doThrow(new DaoException()).when(masterServiceDao).update(getTestMasterService());
        assertThrows(ServiceException.class, () -> masterServiceService.save(getTestMasterService()));
        verifyTransactionStart();
        verifyTransactionRollback();
    }

    @Test
    void delete() throws ServiceException, TransactionException, DaoException {
        masterServiceService.delete(ID_VALUE);
        verify(transaction).start();
        verify(masterServiceDao).removeById(ID_VALUE);
        verify(transaction).commit();
    }

    @Test
    void deleteThrowsException() throws TransactionException, DaoException {
        doThrow(new DaoException()).when(masterServiceDao).removeById(ID_VALUE);
        assertThrows(ServiceException.class, () -> masterServiceService.delete(ID_VALUE));
        verify(transaction).start();
        verify(masterServiceDao).removeById(ID_VALUE);
        verifyTransactionRollback();
    }
}