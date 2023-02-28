package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.controller.command.user.AbstractScheduleCommand.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

class EditScheduleCommandTest extends AbstractCommandTest {

    @Mock
    private WorkingDayService workingDayService;

    private String[] dates = new String[]{"2023-03-01", "2023-03-02"};

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
        when(request.getParameter(ACTION)).thenReturn(DELETE);
        command.process();
        verify(workingDayService).deleteWorkingDaysByUserIdAndDatesArray(ID_VALUE, dates);
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
        doThrow(ServiceException.class).when(workingDayService).deleteWorkingDaysByUserIdAndDatesArray(ID_VALUE, dates);
        command.process();
        verifyError500();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(DAYS)).thenReturn("2023-03-01, 2023-03-02");
        when(request.getParameter(TIME_START)).thenReturn(START_TIME_VALUE.toString());
        when(request.getParameter(TIME_END)).thenReturn(END_TIME_VALUE.toString());
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
    }

    @Override
    protected void verifyAttributes() {

    }
}