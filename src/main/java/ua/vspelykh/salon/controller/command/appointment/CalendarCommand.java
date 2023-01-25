package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
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
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.command.CommandNames.CALENDAR;
import static ua.vspelykh.salon.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.TimeSlotsUtils.getSlots;
import static ua.vspelykh.salon.util.TimeSlotsUtils.removeOccupiedSlots;

public class CalendarCommand extends Command {

    public static final String DAY = "day";
    protected static final String TIME = "time";
    private static final String USER = "user";
    private static final String datePattern = "dd-MM-yyyy";
    private static final String SLOTS = "slots";
    private static final String PLACEHOLDER = "placeholder";
    public static final int INTERVAL = 30;

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
            e.printStackTrace();
        }
        forward(CALENDAR);
    }

    private void addTimeSlotsToAttributes(WorkingDay day) throws ServiceException {
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlots(slots, appointmentService.getByDateAndMasterId(day.getDate(), day.getUserId()), INTERVAL);
        request.setAttribute(SLOTS, slots);
    }

}