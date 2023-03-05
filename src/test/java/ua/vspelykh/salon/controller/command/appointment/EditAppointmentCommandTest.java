package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.*;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.DATE_VALUE;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestMaster;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestAppointment;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUser;

class EditAppointmentCommandTest extends AbstractCommandTest {

    private Appointment appointment;
    private final LocalDateTime newDate = LocalDateTime.of(DATE_VALUE.toLocalDate(), DATE_VALUE.plusHours(3).toLocalTime());

    @Mock
    private AppointmentService appointmentService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new EditAppointmentCommand());
        prepareMocks();
    }

    @Test
    void processIsAdmin() throws ServletException, IOException {
        User user = getTestUser();
        user.setRoles(Set.of(Role.ADMINISTRATOR));
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);
        command.process();
        assertEquals(AppointmentStatus.SUCCESS, appointment.getStatus());
        assertEquals(PaymentStatus.PAID_BY_CARD, appointment.getPaymentStatus());
        assertEquals(newDate, appointment.getDate());
        verifyRedirectAnyString();
    }

    @Test
    void processIsMaster() throws ServletException, IOException {
        User user = getTestUser();
        user.setRoles(Set.of(Role.HAIRDRESSER));
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);
        when(request.getParameter(REDIRECT)).thenReturn(null);
        command.process();
        assertEquals(AppointmentStatus.SUCCESS, appointment.getStatus());
        assertEquals(PaymentStatus.NOT_PAID, appointment.getPaymentStatus());
        assertNotEquals(newDate, appointment.getDate());
        verifyRedirectAnyString();
    }

    @Test
    void processIsMasterAndStatusIsCancelled() throws ServletException, IOException {
        User user = getTestUser();
        user.setRoles(Set.of(Role.HAIRDRESSER));
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);
        when(request.getParameter(STATUS)).thenReturn(AppointmentStatus.CANCELLED.name());
        command.process();
        assertNotEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
    }

    @Test
    void processStatusCancelledAndPaid() throws ServletException, IOException {
        User user = getTestUser();
        user.setRoles(Set.of(Role.ADMINISTRATOR));
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);
        when(request.getParameter(STATUS)).thenReturn(AppointmentStatus.CANCELLED.name());
        appointment.setPaymentStatus(PaymentStatus.PAID_BY_CARD);
        command.process();
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertEquals(PaymentStatus.RETURNED, appointment.getPaymentStatus());
    }

    @Test
    void processIsHairdresserAndStatusIsCancelled() throws ServletException, IOException {
        User user = getTestUser();
        user.setRoles(Set.of(Role.ADMINISTRATOR));
        when(session.getAttribute(CURRENT_USER)).thenReturn(user);
        when(request.getParameter(STATUS)).thenReturn(AppointmentStatus.CANCELLED.name());
        appointment.setPaymentStatus(PaymentStatus.PAID_BY_CARD);
        command.process();
        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        assertEquals(PaymentStatus.RETURNED, appointment.getPaymentStatus());
    }

    @Test
    void processError() throws IOException, ServiceException, ServletException {
        when(session.getAttribute(CURRENT_USER)).thenReturn(getTestMaster());
        when(appointmentService.findById(ID_VALUE)).thenThrow(ServiceException.class);
        command.process();
        verifyError500();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        appointment = getTestAppointment();
        appointment.setStatus(AppointmentStatus.RESERVED);
        appointment.setPaymentStatus(PaymentStatus.NOT_PAID);
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(request.getParameter(APPOINTMENT_ID)).thenReturn(String.valueOf(ID_VALUE));
        when(appointmentService.findById(ID_VALUE)).thenReturn(appointment);
        when(request.getParameter(STATUS)).thenReturn(AppointmentStatus.SUCCESS.name());
        when(request.getParameter(PAYMENT_STATUS)).thenReturn(PaymentStatus.PAID_BY_CARD.name());
        when(request.getParameter(REDIRECT)).thenReturn(REDIRECT);
        when(request.getParameter(NEW_SLOT)).thenReturn(newDate.toLocalTime().toString());
        doNothing().when(appointmentService).save(appointment);
    }

    @Override
    protected void verifyAttributes() {

    }
}