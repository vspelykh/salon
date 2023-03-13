package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENT;
import static ua.vspelykh.salon.controller.command.CommandNames.POSTPONE_FORM;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.util.SalonUtils.getStringDate;
import static ua.vspelykh.salon.util.TimeSlotsUtils.*;

/**
 * Class prepares a form for postponing an appointment. It retrieves an appointment object from the database using
 * the appointment id parameter, and sets the possible working days and time slots for the appointment.
 * <p>
 * It then forwards the request to the PostponeForm JSP for display to the user.
 *
 * @version 1.0
 */
public class PostponeFormCommand extends Command {

    /**
     * Retrieves the appointment object from the database using the appointment id parameter, and sets the possible
     * working days and time slots for the appointment.
     * Forwards the request to the PostponeForm JSP for display to the user.
     * Throws a ServletException or IOException if there is a problem processing the request.
     *
     * @throws ServletException if there is a problem with the servlet or JSP
     * @throws IOException      if there is an I/O problem
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            int appointmentId = getParameterInt(ID);
            Appointment appointment = serviceFactory.getAppointmentService().findById(appointmentId);
            setPossibleWorkingDays(appointment);
            setPossibleTimeSlots(appointment);
            forward(POSTPONE_FORM);
        } catch (ServiceException e) {
            sendError404();
        }
    }

    /**
     * Retrieves the working days for the appointment's master from the database and sets them as a request attribute.
     * Throws a ServiceException if there is a problem retrieving the working days.
     *
     * @param appointment the appointment for which to retrieve the possible working days
     * @throws ServiceException if there is a problem retrieving the working days
     */
    private void setPossibleWorkingDays(Appointment appointment) throws ServiceException {
        List<WorkingDay> workingDays = serviceFactory.getWorkingDayService().findByUserId(appointment.getMasterId());
        setRequestAttribute(DAYS, workingDays);
    }

    /**
     * Sets the appointment and a placeholder string as request attributes, and retrieves the working day for the
     * appointment's master and date, if the DAY parameter is present in the request.
     * If the DAY parameter is not present, sets a default placeholder string.
     * Adds the available time slots for the appointment and working day as request attributes.
     * Throws a ServiceException if there is a problem retrieving the working day or appointments for the day.
     *
     * @param appointment the appointment for which to retrieve the possible time slots
     * @throws ServiceException if there is a problem retrieving the working day or appointments for the day
     */
    private void setPossibleTimeSlots(Appointment appointment) throws ServiceException {
        setRequestAttribute(APPOINTMENT, appointment);
        if (request.getParameter(DAY) != null) {
            WorkingDay day = serviceFactory.getWorkingDayService().getByUserIdAndDate(appointment.getMasterId(),
                    getParameterLocalDate(DAY));
            setRequestAttribute(DAY, day);
            setRequestAttribute(PLACEHOLDER, getStringDate(day.getDate()));
            addTimeSlotsToAttributes(day, appointment);
        } else {
            String placeholder = getLocale().equals(UA_LOCALE) ? PLACEHOLDER_UA : PLACEHOLDER_EN;
            setRequestAttribute(PLACEHOLDER, placeholder);
        }
    }


    /**
     * Retrieves the available time slots for the working day and appointment, removes the time slots that are already
     * taken, and sets the available time slots as a request attribute.
     * Throws a ServiceException if there is a problem retrieving the appointments for the day.
     *
     * @param day         the working day for which to retrieve the available time slots
     * @param appointment the appointment for which to retrieve the possible time slots
     * @throws ServiceException if there is a problem retrieving the appointments
     */
    private void addTimeSlotsToAttributes(WorkingDay day, Appointment appointment) throws ServiceException {
        List<LocalTime> slots = getSlots(day.getTimeStart(), day.getTimeEnd(), INTERVAL);
        List<Appointment> appointments = getServiceFactory().getAppointmentService().getByDateAndMasterId(day.getDate(),
                day.getUserId());
        appointments.remove(appointment);
        removeOccupiedSlots(slots, appointments, INTERVAL);
        List<LocalTime> possibleSlotsForAppointment = getPossibleSlotsForAppointment(appointment.getContinuance(), slots);
        removeSlotsIfDateIsToday(possibleSlotsForAppointment, day.getDate());
        possibleSlotsForAppointment.remove(appointment.getDate().toLocalTime());
        setRequestAttribute(SLOTS, possibleSlotsForAppointment);
    }
}
