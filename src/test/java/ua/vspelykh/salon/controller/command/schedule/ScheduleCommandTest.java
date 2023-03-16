package ua.vspelykh.salon.controller.command.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.USER_LEVEL;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUserLevel;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestWorkingDay;

class ScheduleCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @Mock
    private WorkingDayService workingDayService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new ScheduleCommand());
        prepareMocks();
    }

    @Test
    void processSchedule() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        verifyForward(SCHEDULE);
    }

    @Test
    void processScheduleError() throws ServletException, IOException, ServiceException {
        when(workingDayService.findByUserId(ID_VALUE)).thenThrow(ServiceException.class);
        command.process();
        verifyError404();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        when(userService.getUserLevelByUserId(ID_VALUE)).thenReturn(getTestUserLevel());
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(workingDayService.findByUserId(ID_VALUE)).thenReturn(List.of(getTestWorkingDay()));
    }

    @Override
    protected void verifyAttributes() {
        String days = "\"" + DATE_VALUE.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + "\"";
        verify(request).setAttribute(DAYS, days);
        verify(request).setAttribute(USER_LEVEL, getTestUserLevel());
    }
}