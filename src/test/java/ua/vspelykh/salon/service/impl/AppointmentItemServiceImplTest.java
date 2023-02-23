package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.service.AppointmentItemService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestAppointmentItem;

class AppointmentItemServiceImplTest extends AbstractServiceTest {

    @Mock
    private AppointmentItemDao appointmentItemDao;

    @InjectMocks
    private AppointmentItemService appointmentItemService = new AppointmentItemServiceImpl();

    @Test
    void save() throws ServiceException, TransactionException, DaoException {
        AppointmentItem appointmentItem = getTestAppointmentItem();
        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(appointmentItemDao).create(appointmentItem);
        doNothing().when(transaction).commit();
        appointmentItemService.save(appointmentItem);
        verifyTransactionStart();
        verify(appointmentItemDao, times(1)).create(appointmentItem);
        verifyTransactionCommit();
    }

    @Test
    void saveThrowsException() throws DaoException, TransactionException {
        AppointmentItem appointmentItem = new AppointmentItem();
        doThrow(new DaoException()).when(appointmentItemDao).create(appointmentItem);
        assertThrows(ServiceException.class, () -> {
            appointmentItemService.save(appointmentItem);
        });
        verifyTransactionStart();
        verifyTransactionRollback();
    }

    @Test
    void getByAppointmentId() throws DaoException, ServiceException {
        List<AppointmentItem> appointmentItemList = List.of(getTestAppointmentItem());
        when(appointmentItemDao.getByAppointmentId(ID_VALUE)).thenReturn(appointmentItemList);
        List<AppointmentItem> result = appointmentItemService.getByAppointmentId(ID_VALUE);
        verify(appointmentItemDao, times(1)).getByAppointmentId(ID_VALUE);
        assertEquals(appointmentItemList, result);
    }

    @Test
    void getByAppointmentIdThrowsException() throws DaoException {
        when(appointmentItemDao.getByAppointmentId(ID_VALUE)).thenThrow(new DaoException());
        Assertions.assertThrows(ServiceException.class, () -> {
            appointmentItemService.getByAppointmentId(ID_VALUE);
        });
        verify(appointmentItemDao, times(1)).getByAppointmentId(ID_VALUE);
    }
}