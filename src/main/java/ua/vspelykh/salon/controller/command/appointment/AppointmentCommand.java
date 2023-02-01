package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.dto.MasterServiceDto;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.*;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENT;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.DAY;
import static ua.vspelykh.salon.controller.command.appointment.CalendarCommand.TIME;
import static ua.vspelykh.salon.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.SalonUtils.getLocalDate;
import static ua.vspelykh.salon.util.SalonUtils.getTime;
import static ua.vspelykh.salon.util.TimeSlotsUtils.countAllowedMinutes;

public class AppointmentCommand extends Command {

    private static final String MASTER = "master";
    private static final String ALLOWED_TIME = "allowedTime";

    private UserService userService = ServiceFactory.getUserService();
    private ServiceService serviceService = ServiceFactory.getServiceService();
    private AppointmentService appointmentService = ServiceFactory.getAppointmentService();
    private WorkingDayService workingDayService = ServiceFactory.getWorkingDayService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            User master = userService.findById(Integer.valueOf(request.getParameter(ID)));
            request.setAttribute(MASTER, master);
            request.setAttribute(ID, request.getParameter(ID));
            request.setAttribute(DAY, request.getParameter(DAY));
            request.setAttribute(TIME, request.getParameter(TIME));
            List<MasterServiceDto> dtos = serviceService.getDTOsByMasterId(master.getId());
            request.setAttribute(SERVICES, dtos);
            request.setAttribute(FIRST, dtos.get(0).getId());
            request.setAttribute(SIZE, dtos.size());
            request.setAttribute(USER_LEVEL, userService.getUserLevelByUserId(master.getId()));
            List<Appointment> appointments =
                    appointmentService.getByDateAndMasterId(getLocalDate(request.getParameter(DAY)), master.getId());
            request.setAttribute(ALLOWED_TIME, countAllowedMinutes(getTime(request.getParameter(TIME)), appointments,
                    workingDayService.getDayByUserIdAndDate(master.getId(), getLocalDate(request.getParameter(DAY)))));
            forward(APPOINTMENT);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
