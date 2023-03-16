package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.GET_SCHEDULE;
import static ua.vspelykh.salon.controller.command.CommandNames.ORDERS;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.exception.Messages.EDIT_APPOINTMENT_ERROR;

/**
 * The EditAppointmentCommand class extends the Command class and handles the edit operation for an appointment.
 * It updates the appointment status, payment status, and time slot based on the request parameters.
 *
 * @version 1.0
 */
public class EditAppointmentCommand extends Command {

    /**
     * This method is responsible for processing the request and updating the appointment object based on the
     * request parameters. It first retrieves the appointment object by its ID using the AppointmentService.
     * Then, it calls several methods to update the appointment's status, payment status, and time slot based
     * on the request parameters.
     * Finally, it saves the appointment object using the AppointmentService and redirects the user to the appropriate page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            Appointment appointment = getServiceFactory().getAppointmentService().findById(getParameterInt(APPOINTMENT_ID));
            setStatus(appointment);
            setPaymentStatus(appointment);
            setNewTimeSlot(appointment);
            getServiceFactory().getAppointmentService().save(appointment);
            redirectByMasterId(getParameter(ID));
        } catch (ServiceException e) {
            setSessionAttribute(MESSAGE, EDIT_APPOINTMENT_ERROR);
            redirect(ERROR_REDIRECT);
        }
    }

    /**
     * This method updates the payment status of the appointment based on the request parameter.
     * If the appointment is already cancelled, the payment status is not updated.
     * If the user is an admin and the payment status request parameter is not null, the payment status is updated.
     *
     * @param appointment the appointment object to be updated
     */
    private void setPaymentStatus(Appointment appointment) {
        if (appointment.getStatus().equals(AppointmentStatus.CANCELLED)) {
            return;
        }
        if (isAdmin() && !isParameterNull(PAYMENT_STATUS)) {
            appointment.setPaymentStatus(PaymentStatus.valueOf(getParameter(PAYMENT_STATUS)));
        }
    }

    /**
     * This method updates the time slot of the appointment based on the request parameter.
     * If the user is an admin and the new time slot request parameter is not null, the time slot is updated.
     *
     * @param appointment the appointment object to be updated
     */
    private void setNewTimeSlot(Appointment appointment) {
        if (isAdmin() && !isParameterNull(NEW_SLOT)) {
            appointment.setDate(LocalDateTime.of(appointment.getDate().toLocalDate(), getParameterLocalTime(NEW_SLOT)));
        }
    }

    /**
     * Updates the status and payment status of the appointment based on the request parameters. If the "status" request
     * parameter is not null, the appointment's status is updated to the value of the parameter. If the status is
     * "cancelled" and the user is not an admin, nothing is updated. If the status is "cancelled" and the payment status
     * is not "NOT_PAID", the payment status is updated to "RETURNED". If the status is "SUCCESS" or "DIDNT_COME", and the
     * appointment date is today or in the past, the appointment status is updated to the requested value. If the
     * appointment date is in the future, nothing is updated.
     *
     * @param appointment the appointment object to be updated
     */
    private void setStatus(Appointment appointment) {
        if (!isParameterNull(STATUS)) {
            String status = getParameter(STATUS);
            if (status.equals(AppointmentStatus.CANCELLED.name()) && !isAdmin()) {
                return;
            }
            if (status.equals(AppointmentStatus.SUCCESS.name()) || status.equals(AppointmentStatus.DIDNT_COME.name())) {
                LocalDate dateOfAppointment = appointment.getDate().toLocalDate();
                LocalDate currentDate = LocalDate.now();
                if (currentDate.isBefore(dateOfAppointment)) {
                    return;
                }
            }
            appointment.setStatus(AppointmentStatus.valueOf(status));
            if (status.equals(AppointmentStatus.CANCELLED.name()) && appointment.getPaymentStatus() != PaymentStatus.NOT_PAID) {
                appointment.setPaymentStatus(PaymentStatus.RETURNED);
            }
        }
    }

    /**
     * This method checks if the current user is an admin.
     *
     * @return true if the current user is an admin.
     */
    private boolean isAdmin() {
        return getCurrentUser().getRoles().contains(Role.ADMINISTRATOR);
    }

    /**
     * Redirects the user to a specific page based on the given master ID and the specified parameters.
     * If the "redirect" parameter is equal to "redirect", the user is redirected to the homepage.
     * Otherwise, the user is redirected to a specific appointment schedule page based on the given master ID
     * and the specified parameters.
     *
     * @param masterId the ID of the master associated with the appointment schedule
     * @throws IOException if an I/O error occurs during the processing of the request
     */
    private void redirectByMasterId(String masterId) throws IOException {
        if (getParameter(REDIRECT) != null && getParameter(REDIRECT).equals(REDIRECT)) {
            redirect(HOME_REDIRECT + COMMAND_PARAM + ORDERS);
        } else {
            redirect(String.format(APPOINTMENT_REDIRECT_PATTERN, HOME_REDIRECT, COMMAND_PARAM, GET_SCHEDULE, ID, masterId,
                    DAYS, getParameter(DAYS)));
        }
    }
}
