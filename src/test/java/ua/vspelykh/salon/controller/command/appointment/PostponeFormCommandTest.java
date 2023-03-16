package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.DATE_VALUE;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.POSTPONE_FORM;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestAppointment;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestWorkingDay;
import static ua.vspelykh.salon.util.SalonUtils.getStringDate;

class PostponeFormCommandTest extends AbstractCommandTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private WorkingDayService workingDayService;

    @BeforeEach
    public void setUp() throws ServiceException {
        super.setUp();
        initCommand(new PostponeFormCommand());
        prepareMocks();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(DAY)).thenReturn(String.valueOf(DATE_VALUE.toLocalDate()));
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        when(appointmentService.findById(ID_VALUE)).thenReturn(getTestAppointment());
        when(workingDayService.getByUserIdAndDate(ID_VALUE, DATE_VALUE.toLocalDate())).thenReturn(getTestWorkingDay());
        when(workingDayService.findByUserId(ID_VALUE)).thenReturn(List.of(getTestWorkingDay()));
    }

    @Test
    void processSuccess() throws ServletException, IOException {
        command.process();

        verifyAttributes();
        verifyForward(POSTPONE_FORM);
    }

    @Test
    void processError() throws ServletException, IOException, ServiceException {
        when(workingDayService.getByUserIdAndDate(ID_VALUE, DATE_VALUE.toLocalDate())).thenThrow(ServiceException.class);

        command.process();

        verifyError404();
    }

    @Test
    void testTimeSlots() throws ServletException, IOException, ServiceException {
        LocalDateTime date = DATE_VALUE.plusDays(1);
        List<Appointment> appointments = new ArrayList<>();
        date = prepareTimeSlotTest(date, appointments);

        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(date.toLocalDate());
        when(request.getParameter(DAY)).thenReturn(String.valueOf(date.toLocalDate()));
        when(workingDayService.getByUserIdAndDate(ID_VALUE, date.toLocalDate())).thenReturn(testWorkingDay);
        when(appointmentService.getByDateAndMasterId(date.toLocalDate(), ID_VALUE)).thenReturn(appointments);
        command.process();

        verify(request).setAttribute(SLOTS, getTestSlots());
    }

    private LocalDateTime prepareTimeSlotTest(LocalDateTime date, List<Appointment> appointments) throws ServiceException {
        Appointment appointment = getTestAppointment();
        appointment.setDate(date);
        appointment.setContinuance(120);
        when(appointmentService.findById(ID_VALUE)).thenReturn(appointment);
        // 9 11 13 15
        appointments.add(appointment);
        for (int i = 0; i < 3; i++) {
            appointment = getTestAppointment();
            appointment.setContinuance(60);
            date = date.plusHours(2);
            appointment.setDate(date);
            appointments.add(appointment);
        }
        return date;
    }


    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(DAYS, List.of(getTestWorkingDay()));
        verify(request).setAttribute(PLACEHOLDER, getStringDate(DATE_VALUE.toLocalDate()));
        verify(request).setAttribute(eq(SLOTS), any());
    }

    private List<LocalTime> getTestSlots() {
        return List.of(
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                LocalTime.of(16, 0),
                LocalTime.of(16, 30),
                LocalTime.of(17, 0),
                LocalTime.of(17, 30),
                LocalTime.of(18, 0)
        );
    }
}