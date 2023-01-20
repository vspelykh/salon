package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENTS;
import static ua.vspelykh.salon.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.SalonUtils.getLocaleDate;

public class EditScheduleCommand extends Command {

    private static final String ACTION = "action";
    private static final String SAVE = "save";
    private static final String DELETE = "delete";
    private static final String TIME_START = "time-start";
    private static final String TIME_END = "time-end";

    private int userId;

    private UserService userService = ServiceFactory.getUserService();
    private WorkingDayService workingDayService = ServiceFactory.getWorkingDayService();
    private AppointmentService appointmentService = ServiceFactory.getAppointmentService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            userId = Integer.parseInt(request.getParameter(ID));
            String[] datesArray = request.getParameter(DAYS).split(", ");
            if (SAVE.equals(request.getParameter(ACTION))) {
                Time timeStart = parseTime(request.getParameter(TIME_START));
                Time timeEnd = parseTime(request.getParameter(TIME_END));
                workingDayService.save(userId, datesArray, timeStart, timeEnd);
            } else if (DELETE.equals(request.getParameter(ACTION))) {
                workingDayService.deleteWorkingDaysByUserIdAndDatesArray(userId, datesArray);
            } else {
                List<Appointment> appointments =
                        appointmentService.getByDateAndMasterId(getLocaleDate(datesArray[0]), userId);
                request.setAttribute(APPOINTMENTS, appointments);
                forward(APPOINTMENTS);
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        redirect(SCHEDULE_REDIRECT + userId);
    }

    private Time parseTime(String time) {
        return Time.valueOf(LocalTime.parse(time));
    }
}
