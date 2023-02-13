package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.ORDERS;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

public class EditAppointmentCommand extends Command {

    private static final String REDIRECT = "redirect";

    @Override
    public void process() throws ServletException, IOException {
        try {
            Appointment appointment = getServiceFactory().getAppointmentService().findById(Integer.valueOf(request.getParameter(APPOINTMENT_ID)));
            setStatus(appointment);
            setPaymentStatus(appointment);
            setNewTimeSlot(appointment);
            getServiceFactory().getAppointmentService().save(appointment);
            String masterId = request.getParameter(ID);
            if (request.getParameter(REDIRECT) != null && request.getParameter(REDIRECT).equals(REDIRECT)) {
                redirect(HOME_REDIRECT + COMMAND_PARAM + ORDERS);
            } else {
                redirect(String.format("%s%s%s&%s=%s&%s=%s", HOME_REDIRECT, COMMAND_PARAM, GET_SCHEDULE, ID, masterId,
                        DAYS, request.getParameter(DAYS)));
            }
        } catch (ServiceException e) {
            response.sendError(500);
        }
    }

    private void setPaymentStatus(Appointment appointment) {
        if (isAdmin() && checkNullParam(request.getParameter(PAYMENT_STATUS))) {
            appointment.setPaymentStatus(PaymentStatus.valueOf(request.getParameter(PAYMENT_STATUS)));
        }
    }

    private void setNewTimeSlot(Appointment appointment) {
        if (isAdmin() && checkNullParam(request.getParameter("new_slot"))) {
            appointment.setDate(LocalDateTime.of(appointment.getDate().toLocalDate(),
                    LocalTime.parse(request.getParameter("new_slot"))));
        }
    }

    private void setStatus(Appointment appointment) {
        if (checkNullParam(request.getParameter(STATUS))) {
            String status = request.getParameter(STATUS);
            if (status.equals(AppointmentStatus.CANCELLED.name()) && !isAdmin()){
                return;
            }
            appointment.setStatus(AppointmentStatus.valueOf(status));
            if (status.equals(AppointmentStatus.CANCELLED.name()) && appointment.getPaymentStatus() != PaymentStatus.NOT_PAID){
                appointment.setPaymentStatus(PaymentStatus.RETURNED);
            }
        }
    }
    @SuppressWarnings("unchecked")
    private boolean isAdmin(){
        Set<Role> roles = (Set<Role>) request.getSession().getAttribute(ROLES);
        return roles.contains(Role.ADMINISTRATOR);
    }
}
