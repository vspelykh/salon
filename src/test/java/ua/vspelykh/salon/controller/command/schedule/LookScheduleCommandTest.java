package ua.vspelykh.salon.controller.command.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.WorkingDay;
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
import static ua.vspelykh.salon.controller.ControllerConstants.USER;
import static ua.vspelykh.salon.controller.command.CommandNames.LOOK_SCHEDULE;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestUser;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestWorkingDay;

class LookScheduleCommandTest extends AbstractCommandTest {

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private UserService userService;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new LookScheduleCommand());
        prepareMocks();
    }

    @Test
    void processLookScheduleError() throws ServiceException, ServletException, IOException {
        when(workingDayService.findByUserId(ID_VALUE)).thenThrow(ServiceException.class);
        command.process();
        verifyError404();
    }

    @Test
    void processLookScheduleDaysIsEmptyString() throws Exception {
        command.process();
        verify(request).setAttribute(DAYS, "");
        verifyAttributes();
        verifyForward(LOOK_SCHEDULE);
    }

    @Test
    void processLookScheduleDays() throws Exception {
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(DATE_VALUE.plusDays(1).toLocalDate());
        when(workingDayService.findByUserId(ID_VALUE)).thenReturn(List.of(getTestWorkingDay(), testWorkingDay));
        command.process();
        String dates = "\"" + DATE_VALUE.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + "\"" +
                ", \"" + testWorkingDay.getDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + "\"";
        verify(request).setAttribute(DAYS, dates);
        verifyAttributes();
        verifyForward(LOOK_SCHEDULE);
    }


    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(userService.findById(ID_VALUE)).thenReturn(getTestUser());
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(USER, getTestUser());
    }
}