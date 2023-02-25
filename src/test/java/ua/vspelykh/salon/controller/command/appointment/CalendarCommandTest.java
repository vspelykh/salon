package ua.vspelykh.salon.controller.command.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import ua.vspelykh.salon.controller.command.AbstractCommandTest;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.FeedbackService;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.TimeSlotsUtils;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.CALENDAR;
import static ua.vspelykh.salon.controller.command.CommandTestData.ID_VALUE_2;
import static ua.vspelykh.salon.controller.command.CommandTestData.getTestMaster;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.DAY;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.INTERVAL;
import static ua.vspelykh.salon.model.dao.Table.FEEDBACKS;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.service.impl.ServiceTestData.getTestFeedbackDto;

class CalendarCommandTest extends AbstractCommandTest {

    @Mock
    private WorkingDayService workingDayService;

    @Mock
    private UserService userService;

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private AppointmentService appointmentService;

    private final User testMaster = getTestMaster();

    @BeforeEach
    protected void setUp() throws ServiceException {
        super.setUp();
        initCommand(new CalendarCommand());
        prepareMocks();
    }

    @Test
    void calendarProcess() throws ServletException, IOException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));
        command.process();
        verifyAttributes();
        verifyForward(CALENDAR);
    }

    @Test
    void calendarProcessUserEqualsToMaster() throws ServletException, IOException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE));
        command.process();
        verifyRedirect(MASTERS_REDIRECT);
    }

    @Test
    void testTimeSlotDayNotChosen() throws ServletException, IOException {
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));
        when(request.getParameter(DAY)).thenReturn(null);
        command.process();
        verify(request).setAttribute("placeholder", "Виберіть дату");
    }

    @ParameterizedTest
    @MethodSource("timeSlotData")
    void testTimeSlotCount(List<Appointment> appointments, List<LocalTime> expectedTimeSlots) throws ServiceException, ServletException, IOException {
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(DATE_VALUE.plusDays(1).toLocalDate());
        testWorkingDay.setUserId(ID_VALUE_2);
        prepareForSlotTest(appointments, testWorkingDay);
        command.process();
        verify(request).setAttribute("slots", expectedTimeSlots);
        verifyForward(CALENDAR);
    }

    @Test
    void testTimeSlotCountIfToday() throws ServiceException, ServletException, IOException {
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setDate(DATE_VALUE.toLocalDate());
        testWorkingDay.setUserId(ID_VALUE_2);
        List<Appointment> appointments = Collections.emptyList();
        prepareForSlotTest(appointments, testWorkingDay);
        List<LocalTime> slots = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);
        slots.removeIf(slotTime -> LocalTime.now().isAfter(slotTime));
        command.process();
        verify(request).setAttribute("slots", slots);
    }

    @Parameterized.Parameters
    private static Collection<Object[]> timeSlotData() {
        Appointment appointment = getTestAppointment();
        appointment.setDate(appointment.getDate().plusDays(1));
        appointment.setContinuance(60);
        Appointment appointment1 = getTestAppointment();
        appointment1.setDate(appointment1.getDate().plusDays(1).plusHours(1));
        Appointment appointment2 = getTestAppointment();
        appointment2.setDate(appointment2.getDate().plusDays(1).plusHours(4));
        appointment2.setContinuance(120);

        List<Appointment> list1 = List.of(appointment);
        List<Appointment> list2 = List.of(appointment, appointment1);
        List<Appointment> list3 = List.of(appointment, appointment2);
        List<Appointment> list4 = List.of(appointment1, appointment2);
        List<Appointment> list5 = List.of(appointment, appointment1, appointment2);

        List<LocalTime> expectedSlots = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);

        List<LocalTime> expectedSlots1 = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);
        expectedSlots1.removeAll(List.of(
                LocalTime.of(9, 0),
                LocalTime.of(9, 30)));

        List<LocalTime> expectedSlots2 = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);
        expectedSlots2.removeAll(List.of(
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0)));
        List<LocalTime> expectedSlots3 = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);
        expectedSlots3.removeAll(List.of(
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30)));
        List<LocalTime> expectedSlots4 = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);
        expectedSlots4.removeAll(List.of(
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30)));
        List<LocalTime> expectedSlots5 = TimeSlotsUtils.getSlots(START_TIME_VALUE, END_TIME_VALUE, INTERVAL);
        expectedSlots5.removeAll(List.of(
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30)));
        return Arrays.asList(new Object[][]{
                {Collections.emptyList(), expectedSlots}, {list1, expectedSlots1}, {list2, expectedSlots2},
                {list3, expectedSlots3}, {list4, expectedSlots4}, {list5, expectedSlots5}
        });
    }

    @ParameterizedTest
    @MethodSource("paginationData")
    void testPaginationCountAndAttrs(int countOfItems, int expectedNumberOfPage) throws ServletException, IOException, ServiceException {
        when(feedbackService.countFeedbacksByMasterId(ID_VALUE_2)).thenReturn(countOfItems);
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));
        command.process();
        verify(request).setAttribute(NUMBER_OF_PAGES, expectedNumberOfPage);
    }

    @Parameterized.Parameters
    private static Collection<Object[]> paginationData() {
        return Arrays.asList(new Object[][]{
                {5, 1}, {10, 2}, {15, 3}, {22, 5}, {34, 7}, {47, 10}
        });
    }

    private void prepareForSlotTest(List<Appointment> appointments, WorkingDay workingDay) throws ServiceException {
        prepareMocks();
        when(request.getParameter(ID)).thenReturn(String.valueOf(ID_VALUE_2));
        when(request.getParameter(DAY)).thenReturn(DATE_VALUE.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)));
        when(workingDayService.getDayByUserIdAndDate(ID_VALUE_2, DATE_VALUE.toLocalDate())).thenReturn(workingDay);
        when(appointmentService.getByDateAndMasterId(workingDay.getDate(), ID_VALUE_2)).thenReturn(appointments);
    }

    @Override
    protected void prepareMocks() throws ServiceException {
        when(serviceFactory.getWorkingDayService()).thenReturn(workingDayService);
        when(serviceFactory.getUserService()).thenReturn(userService);
        when(serviceFactory.getFeedbackService()).thenReturn(feedbackService);
        when(serviceFactory.getAppointmentService()).thenReturn(appointmentService);
        when(request.getSession().getAttribute(CURRENT_USER)).thenReturn(getTestUser());
        when(workingDayService.findDaysByUserId(ID_VALUE_2)).thenReturn(List.of(getTestWorkingDay()));
        when(userService.findById(ID_VALUE_2)).thenReturn(testMaster);
        when(feedbackService.getFeedbacksByMasterId(ID_VALUE_2, 1)).thenReturn(List.of(getTestFeedbackDto()));
    }

    @Override
    protected void verifyAttributes() {
        verify(request).setAttribute(USER, testMaster);
        verify(request).setAttribute(DAYS, List.of(getTestWorkingDay()));
        verify(request).setAttribute(FEEDBACKS, List.of(getTestFeedbackDto()));
        verify(request).setAttribute(PAGE + CHECKED, 1);
    }
}