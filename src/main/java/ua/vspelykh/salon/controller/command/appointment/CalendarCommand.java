package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.command.CommandNames.CALENDAR;
import static ua.vspelykh.salon.dao.mapper.Column.ID;

public class CalendarCommand extends Command {

    private static final String DAY = "day";
    private static final String USER = "user";
    private static final String datePattern = "dd-MM-yyyy";
    private static final String SLOTS = "slots";
    private static final String PLACEHOLDER = "placeholder";
    private static final int INTERVAL = 30;

    private WorkingDayService workingDayService = ServiceFactory.getWorkingDayService();
    private UserService userService = ServiceFactory.getUserService();
    private AppointmentService appointmentService = ServiceFactory.getAppointmentService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            List<WorkingDay> workingDays = workingDayService.findDaysByUserId(Integer.valueOf(request.getParameter(ID)));
            request.setAttribute(USER, userService.findById(Integer.valueOf(request.getParameter(ID))));
            request.setAttribute(DAYS, workingDays);
            if (request.getParameter(DAY) != null) {
                WorkingDay day = workingDayService.getDayByUserIdAndDate(Integer.parseInt(request.getParameter(ID)),
                        LocalDate.parse(request.getParameter(DAY), DateTimeFormatter.ofPattern(datePattern)));
                request.setAttribute(DAY, day);
                request.setAttribute(PLACEHOLDER, day.getDate().format(DateTimeFormatter.ofPattern(datePattern)));
                addTimeSlotsToAttributes(day);
            } else {
                request.setAttribute(PLACEHOLDER, "Pick A Date");
            }
        } catch (ServiceException e) {
//            e.printStackTrace();
        }
        forward(CALENDAR);
    }

    private void addTimeSlotsToAttributes(WorkingDay day) throws ServiceException {
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd());
        removeOccupiedSlots(slots, appointmentService.getByDateAndMasterId(day.getDate(), day.getUserId()));
        request.setAttribute(SLOTS, slots);
    }

    private List<LocalTime> getSlots(LocalTime timeStart, LocalTime timeEnd) {
        List<LocalTime> slots = new ArrayList<>();
        slots.add(timeStart);
        while (timeStart.isBefore(timeEnd.minusMinutes(INTERVAL))) {
            timeStart = timeStart.plusMinutes(INTERVAL);
            slots.add(timeStart);
        }
        return slots;
    }

    private void removeOccupiedSlots(List<LocalTime> slots, List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            LocalTime startTime = appointment.getDate().toLocalTime();
            double d = (double) appointment.getContinuance() / (double) INTERVAL;
            int countOfSlots = (int) Math.ceil(d);
            List<LocalTime> slotsForRemove = new ArrayList<>();
            slotsForRemove.add(startTime);
            LocalTime copy = LocalTime.of(startTime.getHour(), startTime.getMinute());
            for (int i = 0; i < countOfSlots - 1; i++) {
                copy = copy.plusMinutes(INTERVAL);
                slotsForRemove.add(copy);
            }
            slots.removeAll(slotsForRemove);
        }
    }
}
//TODO: Super, slots are done! Need to check it with different continuance. Set date as value in the input field,
// Form for appointment.
