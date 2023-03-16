package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.SalonUtils.getTime;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_POSTPONEMENT_FAIL;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_POSTPONEMENT_SUCCESS;

/**
 * Command that processes the data submitted by the user from the POSTPONE form, checks if the chosen time slot
 * is available, and saves the appointment if it is valid.
 *
 * @version 1.0
 */
public class PostponementCommand extends Command {

    /**
     * Processes the data submitted by the user, updates the appointment with the new date and time, validates the new
     * time slot, and saves the appointment if it is valid. If an error occurs, an error message is sent and the user
     * is redirected to the error page.
     *
     * @throws ServletException if there is an error in the servlet
     * @throws IOException      if there is an error with input/output
     */
    @Override
    public void process() throws ServletException, IOException {
        LocalDate date = getParameterLocalDate(DAY);
        LocalTime time = getParameterLocalTime(TIME);
        try {
            Appointment appointment = updateDateTimeOfAppointment(date, time);
            validateNewTimeSlot(appointment, date, time);
            serviceFactory.getAppointmentService().save(appointment);
            setSessionAttribute(MESSAGE, MESSAGE_POSTPONEMENT_SUCCESS);
            redirect(SUCCESS_REDIRECT);
        } catch (ServiceException e) {
            setSessionAttribute(MESSAGE, e.getMessage());
            redirect(ERROR_REDIRECT);
        }
    }

    /**
     * Updates the date and time of the appointment with the given date and time.
     *
     * @param date the new date of the appointment
     * @param time the new time of the appointment
     * @return the updated appointment
     * @throws ServiceException if there is an error with the service
     */
    private Appointment updateDateTimeOfAppointment(LocalDate date, LocalTime time) throws ServiceException {
        Appointment appointment = serviceFactory.getAppointmentService().findById(getParameterInt(ID));
        appointment.setDate(LocalDateTime.of(date, time));
        return appointment;
    }

    /**
     * Validates the new time slot for the appointment by checking if it is available. The method retrieves the
     * working day and appointment information for the given date and master ID, and calculates the available time
     * slots based on the working day schedule and the appointments already scheduled for that day. If the chosen
     * time slot is not available or if it would result in an appointment running over the allotted time, a
     * ServiceException is thrown with the appropriate error message.
     *
     * @param appointment the appointment to check
     * @param date        the new date of the appointment
     * @param time        the new time of the appointment
     * @throws ServiceException if the chosen time slot is not available or if it would result in an appointment
     *                          running over the allotted time
     */
    private void validateNewTimeSlot(Appointment appointment, LocalDate date, LocalTime time) throws ServiceException {
        WorkingDay day = getServiceFactory().getWorkingDayService().getByUserIdAndDate(appointment.getMasterId(), date);
        List<Appointment> appointments = getServiceFactory().getAppointmentService()
                .getByDateAndMasterId(date, appointment.getMasterId());
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        removeOccupiedSlots(slots, getServiceFactory().getAppointmentService().getByDateAndMasterId(day.getDate(),
                day.getUserId()), INTERVAL);
        removeSlotsIfDateIsToday(slots, day.getDate());
        int allowedMinutes = countAllowedMinutes(getTime(String.valueOf(time)), appointments, day);
        if (!slots.contains(time) || allowedMinutes < appointment.getContinuance()) {
            throw new ServiceException(MESSAGE_POSTPONEMENT_FAIL);
        }
    }
}
