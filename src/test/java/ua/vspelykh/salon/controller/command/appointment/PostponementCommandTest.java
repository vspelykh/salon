package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.Messages;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.DATE_VALUE;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestAppointment;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestWorkingDay;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_POSTPONEMENT_FAIL;

class PostponementCommandTest extends AbstractCommandTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private WorkingDayService workingDayService;

    @BeforeEach
    public void setUp() throws ServiceException {
        super.setUp();
        initCommand(new PostponementCommand());
        prepareMocks();
    }

    @Test
    void processValidAppointment() throws ServiceException, ServletException, IOException {
        command.process();

        Appointment testAppointment = getTestAppointment();
        testAppointment.setDate(DATE_VALUE.plusDays(1).plusHours(5));
        verify(appointmentService).save(testAppointment);
        verify(session).setAttribute(MESSAGE, Messages.MESSAGE_POSTPONEMENT_SUCCESS);
        verifyRedirect(SUCCESS_REDIRECT);
    }

    @Test
    void processInvalidAppointment() throws ServiceException, ServletException, IOException {
        Appointment testAppointment = getTestAppointment();
        testAppointment.setDate(DATE_VALUE.plusDays(1));
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(testAppointment);
        when(appointmentService.getByDateAndMasterId(any(), anyInt())).thenReturn(appointments);

        Appointment postponedAppointment = getTestAppointment();
        when(appointmentService.findById(ID_VALUE)).thenReturn(postponedAppointment);

        when(request.getParameter(DAY)).thenReturn(String.valueOf(DATE_VALUE.plusDays(1).toLocalDate()));
        when(request.getParameter(TIME)).thenReturn(String.valueOf(DATE_VALUE.toLocalTime()));

        command.process();

        verify(session).setAttribute(MESSAGE, MESSAGE_POSTPONEMENT_FAIL);
        verifyRedirect(ERROR_REDIRECT);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        when(request.getParameter(DAY)).thenReturn(String.valueOf(DATE_VALUE.plusDays(1).toLocalDate()));
        when(request.getParameter(TIME)).thenReturn(String.valueOf(DATE_VALUE.plusHours(5).toLocalTime()));
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(appointmentService.getByDateAndMasterId(any(), anyInt())).thenReturn(Collections.emptyList());
        when(appointmentService.findById(ID_VALUE)).thenReturn(getTestAppointment());
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(DATE_VALUE.toLocalDate().plusDays(1));
        when(workingDayService.getByUserIdAndDate(anyInt(), any())).thenReturn(testWorkingDay);
    }

    @Override
    protected void verifyAttributes() {

    }
}