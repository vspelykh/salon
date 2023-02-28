package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.*;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENT;
import static ua.vspelykh.salon.controller.command.CommandTestData.ID_VALUE_2;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestMaster;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.DAY;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.TIME;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.MASTER_ID;

class CreateAppointmentCommandTest extends AbstractCommandTest {

    @Mock
    private MasterServiceService masterServiceService;

    @Mock
    BaseServiceService baseServiceService;

    @Mock
    private UserService userService;

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private AppointmentService appointmentService;

    LocalDate date = DATE_VALUE.plusDays(1).toLocalDate();

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new CreateAppointmentCommand());
    }

    @Test
    void createAppointmentProcess() throws ServiceException, ServletException, IOException {
        prepareMocks();
        command.process();
        verifyAttributes();
        verifyRedirect(SUCCESS_REDIRECT);
    }

    @Test
    void createAppointmentProcessNotAllowed() throws ServiceException, ServletException, IOException {
        prepareMocks();
        Appointment testAppointment = getTestAppointment();
        testAppointment.setDate(LocalDateTime.of(date, DATE_VALUE.toLocalTime()));
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(testAppointment);
        when(appointmentService.getByDateAndMasterId(date, ID_VALUE_2)).thenReturn((appointments));
        command.process();
        verify(response, never()).sendRedirect(SUCCESS_REDIRECT);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        String services = ".|.|.|1";
        when(request.getParameterValues(SERVICES)).thenReturn(new String[]{services});
        when(serviceFactory.getServiceService()).thenReturn(masterServiceService);
        when(serviceFactory.getBaseServiceService()).thenReturn(baseServiceService);
        when(baseServiceService.findById(ID_VALUE)).thenReturn(getTestBaseService());
        when(masterServiceService.findById(ID_VALUE)).thenReturn(getTestMasterService());
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(request.getParameter(MASTER_ID)).thenReturn(String.valueOf(ID_VALUE_2));
        User client = getTestUser();
        when(session.getAttribute(CURRENT_USER)).thenReturn(client);
        User master = getTestMaster();
        when(userService.findById(ID_VALUE_2)).thenReturn(master);
        when(userService.getUserLevelByUserId(ID_VALUE_2)).thenReturn(getTestUserLevel());
        when(request.getParameter(DAY)).thenReturn((date.format(DateTimeFormatter.ofPattern(DATE_PATTERN))));
        when(request.getParameter(TIME)).thenReturn(DATE_VALUE.toLocalTime().toString());
        when(request.getParameter(PAYMENT)).thenReturn(PaymentStatus.PAID_BY_CARD.name());
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(testWorkingDay.getDate().plusDays(1));
        when(workingDayService.getDayByUserIdAndDate(ID_VALUE_2, date)).thenReturn(testWorkingDay);
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        Appointment testAppointment = getTestAppointment();
        testAppointment.setDate(LocalDateTime.of(date, DATE_VALUE.toLocalTime()));
        doNothing().when(appointmentService).save(testAppointment, List.of(getTestMasterService()));
    }

    @Override
    protected void verifyAttributes() {
        verify(session).setAttribute(MESSAGE, APPOINTMENT + DOT + SUCCESS);
    }
}