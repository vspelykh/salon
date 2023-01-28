package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.service.AppointmentService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.dao.mapper.Column.*;

public class EditAppointmentCommand extends Command {

    private AppointmentService appointmentService = ServiceFactory.getAppointmentService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            Appointment appointment = appointmentService.findById(Integer.valueOf(request.getParameter(APPOINTMENT_ID)));
            appointment.setStatus(AppointmentStatus.valueOf(request.getParameter(STATUS)));
            appointmentService.save(appointment);
            String masterId = request.getParameter(ID);
            redirect(String.format("%s%s%s&%s=%s&%s=%s", HOME_REDIRECT, COMMAND_PARAM, GET_SCHEDULE, ID, masterId,
                    DAYS, request.getParameter(DAYS)));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
