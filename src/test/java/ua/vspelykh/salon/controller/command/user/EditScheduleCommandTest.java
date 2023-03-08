package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.controller.command.user.AbstractScheduleCommand.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestAppointment;

class EditScheduleCommandTest extends AbstractCommandTest {

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private AppointmentService appointmentService;

    private final String[] dates = new String[]{"01-03-2023", "02-03-2023"};

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new EditScheduleCommand());
        prepareMocks();
    }

    @Test
    void processEditScheduleSaveDays() throws ServletException, IOException, ServiceException {
        when(request.getParameter(ACTION)).thenReturn(SAVE);
        command.process();
        verify(workingDayService).save(ID_VALUE, dates, Time.valueOf(START_TIME_VALUE), Time.valueOf(END_TIME_VALUE));
        verifyRedirect(SCHEDULE_REDIRECT + ID_VALUE);
    }

    @Test
    void processEditScheduleDeleteDays() throws ServletException, IOException, ServiceException {
        when(appointmentService.getByDateAndMasterId(any(), anyInt())).thenReturn(Collections.emptyList());
        when(request.getParameter(ACTION)).thenReturn(DELETE);
        command.process();
        verify(workingDayService).deleteByUserIdAndDatesArray(ID_VALUE, dates);
        verifyRedirect(SCHEDULE_REDIRECT + ID_VALUE);
    }

    @Test
    void processEditScheduleError() throws ServletException, IOException {
        command.process();
        verifyError404();
    }

    @Test
    void processEditScheduleDBError() throws ServletException, IOException, ServiceException {
        when(request.getParameter(ACTION)).thenReturn(DELETE);
        doThrow(ServiceException.class).when(appointmentService).getByDateAndMasterId(any(), anyInt());
        command.process();
        verifyError500();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(appointmentService.getByDateAndMasterId(any(), anyInt())).thenReturn(List.of(getTestAppointment()));
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(DAYS)).thenReturn("01-03-2023, 02-03-2023");
        when(request.getParameter(TIME_START)).thenReturn(START_TIME_VALUE.toString());
        when(request.getParameter(TIME_END)).thenReturn(END_TIME_VALUE.toString());
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
    }

    @Override
    protected void verifyAttributes() {

    }
}