package ua.vspelykh.salon.controller.command.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.ScheduleBuilder;
import ua.vspelykh.salon.util.ScheduleItem;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.DATE_PATTERN;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.controller.filter.LocalizationFilter.LANG;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestAppointmentDto;

class GetScheduleCommandTest extends AbstractCommandTest {

    @Mock
    private UserService userService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private BaseServiceService baseServiceService;

    private final String date = DATE_VALUE.plusDays(1).toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN));

    private List<AppointmentDto> appointments;

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new GetScheduleCommand());
        prepareMocks();
    }

    @Test
    void processGetSchedule() throws ServletException, IOException {
        command.process();
        verifyAttributes();
        verifyForward(GET_SCHEDULE);
    }

    @Test
    void processGetScheduleError() throws ServletException, IOException, ServiceException {
        when(userService.findById(ID_VALUE)).thenThrow(ServiceException.class);
        command.process();
        verifyError500();
    }

    @Test
    void testScheduleBuilder() throws ServiceException {
        prepareScheduleBuilderData();
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(DATE_VALUE.plusDays(1).toLocalDate());
        ScheduleBuilder builder = new ScheduleBuilder(appointments, testWorkingDay, UA_LOCALE, serviceFactory);
        List<ScheduleItem> scheduleItems = builder.build();
        verifyScheduleBuilder(scheduleItems);
    }

    private void verifyScheduleBuilder(List<ScheduleItem> actualScheduleItems) {
        List<ScheduleItem> expectedScheduleItems = new ArrayList<>();
        for (AppointmentDto appointment : appointments) {
            ScheduleItem scheduleItem = ScheduleItem.builder().appointment(appointment)
                    .start(appointment.getDate().toLocalTime())
                    .end(appointment.getDate().toLocalTime().plusMinutes(INTERVAL))
                    .info(SERVICE_UA_VALUE)
                    .build();
            expectedScheduleItems.add(scheduleItem);
        }

        List<ScheduleItem> freeSlots = List.of(
                freeSlot(LocalTime.of(8, 0), 60),
                freeSlot(LocalTime.of(9, 30), 90),
                freeSlot(LocalTime.of(11, 30), 90),
                freeSlot(LocalTime.of(13, 30), 90),
                freeSlot(LocalTime.of(15, 30), 90),
                freeSlot(LocalTime.of(17, 30), 90),
                freeSlot(LocalTime.of(19, 30), 30));
        expectedScheduleItems.addAll(freeSlots);
        expectedScheduleItems.sort(Comparator.comparing(ScheduleItem::getStart));

        assertEquals(expectedScheduleItems, actualScheduleItems);
    }

    private void prepareScheduleBuilderData() {
        appointments = new ArrayList<>();
        appointments.add(getTestAppointmentDto());
        for (int i = 1; i <= 5; i++) {
            AppointmentDto appointment = getTestAppointmentDto();
            appointment.setStatus(AppointmentStatus.RESERVED);
            appointment.setDate(DATE_VALUE.plusDays(1).plusHours(i * 2));
            appointments.add(appointment);
        }
    }

    private ScheduleItem freeSlot(LocalTime start, int interval) {
        return ScheduleItem.builder().start(start)
                .end(start.plusMinutes(interval))
                .info("Free slot")
                .build();
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        when(request.getParameter(DAYS)).thenReturn(date);
        when(session.getAttribute(LANG)).thenReturn(UA_LOCALE);
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(userService.findById(ID_VALUE)).thenReturn(getTestUser());
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(appointmentService.getDTOsByDateAndMasterId(DATE_VALUE.toLocalDate(), ID_VALUE))
                .thenReturn(List.of(getTestAppointmentDto()));
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        when(workingDayService.getByUserIdAndDate(ID_VALUE, DATE_VALUE.toLocalDate())).thenReturn(getTestWorkingDay());
        when(serviceFactory.getBaseServiceService()).thenReturn(baseServiceService);
        when(baseServiceService.findById(ID_VALUE)).thenReturn(getTestBaseService());
        when(session.getAttribute(CURRENT_USER)).thenReturn(getTestUser());
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(eq(FREE_SLOTS_MAP), any());
        verify(request).setAttribute(STATUS, AppointmentStatus.values());
        verify(request).setAttribute(eq(SCHEDULE), any());
        verify(request).setAttribute(USER, getTestUser());
    }
}