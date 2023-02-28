package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.*;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.*;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestAppointmentDto;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestMaster;

class AppointmentServiceImplTest extends AbstractServiceTest {

    @Mock
    private AppointmentDao appointmentDao;

    @Mock
    private UserDao userDao;

    @Mock
    private AppointmentItemDao appointmentItemDao;

    @InjectMocks
    private AppointmentService appointmentService = new AppointmentServiceImpl();

    @Test
    void findById() throws DaoException, ServiceException {
        when(appointmentDao.findById(ID_VALUE)).thenReturn(getTestAppointment());
        Appointment result = appointmentService.findById(ID_VALUE);

        assertEquals(result, getTestAppointment());
    }

    @Test
    void saveNewAppointment() throws DaoException, TransactionException, ServiceException {
        Appointment appointment = getTestAppointment();
        appointment.setId(null);

        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(appointmentDao).create(appointment);
        doNothing().when(transaction).commit();

        appointmentService.save(appointment);

        verifyTransactionStart();
        verify(appointmentDao, times(1)).create(appointment);
        verifyTransactionCommit();
    }

    @Test
    void saveExistingAppointment() throws DaoException, TransactionException, ServiceException {
        Appointment appointment = getTestAppointment();

        doNothing().when(transaction).start();
        doNothing().when(appointmentDao).update(appointment);
        doNothing().when(transaction).commit();

        appointmentService.save(appointment);

        verifyTransactionStart();
        verify(appointmentDao).update(appointment);
        verifyTransactionCommit();
    }

    @Test
    void saveNewAppointmentWithServices() throws ServiceException, DaoException, TransactionException {
        appointmentService.save(getTestAppointment(), List.of(getTestMasterService()));

        verify(appointmentDao, never()).create(any(Appointment.class));
        verify(appointmentDao).update(getTestAppointment());
        verify(appointmentItemDao).create(any(AppointmentItem.class));
        verifyTransactionStart();
        verifyTransactionCommit();
        verify(transaction, never()).rollback();
    }

    @Test
    void saveNewAppointmentWithServicesThrowsException() throws DaoException, TransactionException {
        Appointment testAppointment = getTestAppointment();
        testAppointment.setId(null);
        doThrow(DaoException.class).when(appointmentDao).create(any(Appointment.class));

        assertThrows(ServiceException.class,
                () -> {
                    appointmentService.save(testAppointment, List.of(getTestMasterService()));
                });

        verify(appointmentDao).create(testAppointment);
        verify(appointmentDao, never()).update(testAppointment);
        verifyTransactionStart();
        verify(transaction, never()).commit();
        verifyTransactionRollback();
    }

    @Test
    void delete() throws ServiceException, TransactionException, DaoException {
        appointmentService.delete(ID_VALUE);
        verify(transaction).start();
        verify(appointmentDao).removeById(ID_VALUE);
        verify(transaction).commit();
    }

    @Test
    void testDeleteTransactionException() throws TransactionException {
        doThrow(TransactionException.class).when(transaction).start();

        assertThrows(ServiceException.class, () -> appointmentService.delete(ID_VALUE));
        verify(transaction).start();
        verify(transaction, never()).commit();
        verify(transaction).rollback();
    }

    @Test
    void getDTOsByDateAndMasterId() throws TransactionException, DaoException, ServiceException {
        doNothing().when(transaction).start();
        when(appointmentDao.getByDateAndMasterId(DATE_VALUE.toLocalDate(), ID_VALUE))
                .thenReturn(List.of(getTestAppointment()));
        doNothing().when(transaction).commit();

        User master = getTestMaster();
        User client = getTestUser();
        client.setId(2);

        when(userDao.findById(1)).thenReturn(master);
        when(userDao.findById(2)).thenReturn(client);

        when(appointmentItemDao.getByAppointmentId(1)).thenReturn(List.of(getTestAppointmentItem()));

        List<AppointmentDto> appointmentDTOs = appointmentService.getDTOsByDateAndMasterId(DATE_VALUE.toLocalDate(), ID_VALUE);

        assertEquals(1, appointmentDTOs.size());

        AppointmentDto appointmentDto = appointmentDTOs.get(0);

        assertEquals(1, appointmentDto.getId());
        assertEquals(1, appointmentDto.getMaster().getId());
        assertEquals(NAME_VALUE, appointmentDto.getMaster().getName());
        assertEquals(2, appointmentDto.getClient().getId());
        assertEquals(NAME_VALUE, appointmentDto.getClient().getName());
        assertEquals(CONTINUANCE_VALUE, appointmentDto.getContinuance());
        assertEquals(DATE_VALUE, appointmentDto.getDate());
        assertEquals(PRICE_VALUE, appointmentDto.getPrice());
        assertEquals(1, appointmentDto.getDiscount());
        assertEquals(List.of(getTestAppointmentItem()), appointmentDto.getAppointmentItems());
        assertEquals(AppointmentStatus.SUCCESS, appointmentDto.getStatus());
        assertEquals(PaymentStatus.PAID_BY_CARD, appointmentDto.getPaymentStatus());
    }

    @Test
    void getFiltered() throws DaoException, ServiceException, TransactionException {

        when(appointmentDao.getFiltered(ID_VALUE, DATE_VALUE.toLocalDate(), DATE_VALUE.toLocalDate(),
                AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD, 1, 5)).thenReturn(List.of(getTestAppointment()));

        User master = getTestMaster();
        User client = getTestUser();
        client.setId(2);

        when(userDao.findById(1)).thenReturn(master);
        when(userDao.findById(2)).thenReturn(client);

        List<AppointmentDto> actualAppointmentDtos = appointmentService.getFiltered(ID_VALUE, DATE_VALUE.toLocalDate(),
                DATE_VALUE.toLocalDate(), AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD, 1, 5);

        assertEquals(List.of(getTestAppointmentDto()), actualAppointmentDtos);
        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void getFilteredThrowsException() throws DaoException, TransactionException, ServiceException {
        when(appointmentDao.getFiltered(ID_VALUE, DATE_VALUE.toLocalDate(), DATE_VALUE.toLocalDate(),
                AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD, 1, 5)).thenThrow(DaoException.class);

        assertThrows(ServiceException.class, () -> appointmentService.getFiltered(ID_VALUE, DATE_VALUE.toLocalDate(),
                DATE_VALUE.toLocalDate(), AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD, 1, 5));

        verifyTransactionStart();
        verifyTransactionRollback();
    }
}