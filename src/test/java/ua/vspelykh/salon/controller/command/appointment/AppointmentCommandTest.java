package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.dto.MasterServiceDto;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.MasterServiceService;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.DATE_PATTERN;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENT;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestMasterServiceDto;

class AppointmentCommandTest extends AbstractCommandTest {

    private final String ALLOWED_TIME = "allowedTime";

    @Mock
    private UserService userService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private MasterServiceService masterServiceService;

    private User master;
    private List<MasterServiceDto> dtos;

    @BeforeEach
    public void setUp() throws ServiceException {
        super.setUp();
        initCommand(new AppointmentCommand());
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(serviceFactory.getServiceService()).thenReturn(masterServiceService);
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
    }

    @Test
    void appointmentProcess() throws Exception {
        prepareMocks();
        command.process();
        verifyAttributes();
        verifyForward(APPOINTMENT);
    }

    @Test
    void appointmentProcessSendError() throws Exception {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(userService.findById(anyInt())).thenThrow(ServiceException.class);
        command.process();
        verifyError404();
    }

    @ParameterizedTest
    @MethodSource("allowedTimeTestData")
    void testAllowedTime(List<Appointment> appointments, Time time, int expectedAllowedTime) throws ServiceException, ServletException, IOException {
        prepareMocks();
        when(appointmentService.getByDateAndMasterId(DATE_VALUE.toLocalDate(), ID_VALUE)).thenReturn(appointments);
        when(request.getParameter(TIME)).thenReturn(time.toString());
        command.process();
        verify(request).setAttribute(ALLOWED_TIME, expectedAllowedTime);
        verifyForward(APPOINTMENT);
    }

    @Test
    void testAllowedTimeSendError() throws ServiceException, ServletException, IOException {
        Appointment testAppointment = getTestAppointment();
        testAppointment.setDate(testAppointment.getDate().plusHours(3));
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(testAppointment);
        Time time = Time.valueOf(LocalTime.of(12, 0));
        prepareMocks();
        when(appointmentService.getByDateAndMasterId(DATE_VALUE.toLocalDate(), ID_VALUE)).thenReturn(appointments);
        when(request.getParameter(TIME)).thenReturn(time.toString());
        command.process();
        verify(request, never()).setAttribute(ALLOWED_TIME, eq(anyInt()));
        verifyError404();
    }

    @Parameterized.Parameters
    private static Collection<Object[]> allowedTimeTestData() {
        Appointment testAppointment = getTestAppointment();
        testAppointment.setDate(testAppointment.getDate().plusHours(3));
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(testAppointment);
        return Arrays.asList(new Object[][]{
                {appointments, Time.valueOf(LocalTime.of(8, 0)), 240},
                {appointments, Time.valueOf(LocalTime.of(8, 30)), 210},
                {appointments, Time.valueOf(LocalTime.of(9, 0)), 180},
                {appointments, Time.valueOf(LocalTime.of(11, 30)), 30},
                {appointments, Time.valueOf(LocalTime.of(18, 0)), 120},
                {appointments, Time.valueOf(LocalTime.of(19, 30)), 30},
        });
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        master = getTestUser();
        when(userService.findById(ID_VALUE)).thenReturn(master);
        when(userService.getUserLevelByUserId(ID_VALUE)).thenReturn(getTestUserLevel());

        when(workingDayService.getByUserIdAndDate(ID_VALUE, DATE_VALUE.toLocalDate())).thenReturn(getTestWorkingDay());

        dtos = List.of(getTestMasterServiceDto());
        when(masterServiceService.getDTOsByMasterId(ID_VALUE, UA_LOCALE)).thenReturn(dtos);

        UserService userService = mock(UserService.class);
        when(userService.getUserLevelByUserId(ID_VALUE)).thenReturn(getTestUserLevel());

        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(DAY)).thenReturn(DATE_VALUE.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        when(request.getParameter(TIME)).thenReturn(START_TIME_VALUE.toString());
        when(request.getSession().getAttribute(LANG)).thenReturn(UA_LOCALE);
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute("master", master);
        verify(request).setAttribute(ID, String.valueOf(ID_VALUE));
        verify(request).setAttribute(DAY, DATE_VALUE.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        verify(request).setAttribute(TIME, START_TIME_VALUE.format(DateTimeFormatter.ofPattern("HH:mm")));
        verify(request).setAttribute(ALLOWED_TIME, 720);
        verify(request).setAttribute("services", dtos);
        verify(request).setAttribute(FIRST, dtos.get(0).getId());
        verify(request).setAttribute(SIZE, dtos.size());
        verify(request).setAttribute(USER_LEVEL, getTestUserLevel());
    }
}