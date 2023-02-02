package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.ORDERS;
import static ua.vspelykh.salon.dao.mapper.Column.*;

public class EditAppointmentCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            Appointment appointment = getServiceFactory().getAppointmentService().findById(Integer.valueOf(request.getParameter(APPOINTMENT_ID)));
            setStatus(appointment);
            setNewTimeSlot(appointment);
            getServiceFactory().getAppointmentService().save(appointment);
            String masterId = request.getParameter(ID);
            if (request.getParameter("redirect") != null) {
                redirect(HOME_REDIRECT + COMMAND_PARAM + ORDERS);
            } else {
                redirect(String.format("%s%s%s&%s=%s&%s=%s", HOME_REDIRECT, COMMAND_PARAM, GET_SCHEDULE, ID, masterId,
                        DAYS, request.getParameter(DAYS)));
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void setNewTimeSlot(Appointment appointment) {
        if (checkNullParam(request.getParameter("new_slot"))) {
            appointment.setDate(LocalDateTime.of(appointment.getDate().toLocalDate(),
                    LocalTime.parse(request.getParameter("new_slot"))));
        }
    }

    private void setStatus(Appointment appointment) {
        if (checkNullParam(request.getParameter(STATUS))) {
            appointment.setStatus(AppointmentStatus.valueOf(request.getParameter(STATUS)));
        }
    }
}
