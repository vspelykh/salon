package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dto.BaseServiceDto;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestBaseService;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestBaseServiceDto;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestCategory;

class BaseServiceServiceImplTest extends AbstractServiceTest {

    @Mock
    private ServiceCategoryDao serviceCategoryDao;

    @Mock
    private BaseServiceDao baseServiceDao;

    @InjectMocks
    private BaseServiceService baseServiceService = new BaseServiceServiceImpl();

    @Test
    void findAll() throws DaoException, ServiceException, TransactionException {
        when(baseServiceDao.findAll()).thenReturn(List.of(getTestBaseService()));
        when(serviceCategoryDao.findById(ID_VALUE)).thenReturn(getTestCategory());
        List<BaseServiceDto> dtos = baseServiceService.findAll(UA_LOCALE);
        assertEquals(getTestBaseServiceDto(), dtos.get(0));
        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void findAllThrowsException() throws DaoException, TransactionException {
        when(baseServiceDao.findAll()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> baseServiceService.findAll(UA_LOCALE));
        verifyTransactionStart();
        verifyTransactionRollback();
    }

    @Test
    void saveNew() throws ServiceException, TransactionException, DaoException {
        BaseService testBaseService = getTestBaseService();
        testBaseService.setId(null);
        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(baseServiceDao).create(testBaseService);
        doNothing().when(transaction).commit();
        baseServiceService.save(testBaseService);
        verifyTransactionStart();
        verify(baseServiceDao, times(1)).create(testBaseService);
        verifyTransactionCommit();
    }

    @Test
    void saveExist() throws ServiceException, TransactionException, DaoException {
        doNothing().when(transaction).start();
        doNothing().when(baseServiceDao).update(getTestBaseService());
        doNothing().when(transaction).commit();
        baseServiceService.save(getTestBaseService());
        verifyTransactionStart();
        verify(baseServiceDao, times(1)).update(getTestBaseService());
        verifyTransactionCommit();
    }

    @Test
    void saveThrowsException() throws DaoException, TransactionException {
        doThrow(new DaoException()).when(baseServiceDao).update(getTestBaseService());
        assertThrows(ServiceException.class, () -> baseServiceService.save(getTestBaseService()));
        verifyTransactionStart();
        verifyTransactionRollback();
    }


    @Test
    void delete() throws ServiceException, TransactionException, DaoException {
        baseServiceService.delete(ID_VALUE);
        verify(transaction).start();
        verify(baseServiceDao).removeById(ID_VALUE);
        verify(transaction).commit();
    }

    @Test
    void deleteThrowsException() throws TransactionException, DaoException {
        doThrow(new DaoException()).when(baseServiceDao).removeById(ID_VALUE);
        assertThrows(ServiceException.class, () -> baseServiceService.delete(ID_VALUE));
        verify(transaction).start();
        verify(baseServiceDao).removeById(ID_VALUE);
        verifyTransactionRollback();
    }

    @Test
    void findByFilter() throws DaoException, TransactionException, ServiceException {
        when(baseServiceDao.findByFilter(List.of(ID_VALUE), 1, 5)).thenReturn(List.of(getTestBaseService()));
        when(serviceCategoryDao.findById(anyInt())).thenReturn(getTestCategory());

        List<BaseServiceDto> dtos = baseServiceService.findByFilter(List.of(ID_VALUE), 1, 5, UA_LOCALE);

        verifyTransactionStart();
        assertEquals(getTestBaseServiceDto(), dtos.get(0));
        assertEquals(1, dtos.size());
        verifyTransactionCommit();
    }

    @Test
    void findByFilterThrowsException() throws DaoException, TransactionException {
        when(baseServiceDao.findByFilter(List.of(ID_VALUE), 1, 5)).thenThrow(DaoException.class);

        assertThrows(ServiceException.class,
                () -> baseServiceService.findByFilter(List.of(ID_VALUE), 1, 5, UA_LOCALE));

        verifyTransactionStart();
        verifyTransactionRollback();
    }
}