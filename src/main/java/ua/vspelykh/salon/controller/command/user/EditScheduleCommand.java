package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.SalonUtils.getLocalDate;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_DELETE_DAYS;

/**
 * The EditScheduleCommand class extends the AbstractScheduleCommand class and is responsible for processing the requests
 * to edit the working schedule of the user with the given ID.
 * <p>
 * It handles two actions: SAVE and DELETE. If the action is 'SAVE', it retrieves the selected days and time range
 * from the request, and saves them to the database. If the action is 'DELETE', it retrieves the selected days from
 * the request and deletes them from the database.
 *
 * @version 1.0
 */
public class EditScheduleCommand extends AbstractScheduleCommand {

    /**
     * This method is responsible for processing the requests to edit the working schedule of the user with the given ID.
     * It retrieves the selected days and time range from the request, and saves them to the database only if the selected
     * dates are today or in the future and the action is 'SAVE'. If the action is 'DELETE', it retrieves the selected days
     * from the request and deletes them from the database.
     * It then redirects the user to the schedule page of the edited user.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        int userId = getParameterInt(ID);
        try {
            String[] datesArray = getParameter(DAYS).split(", ");
            if (SAVE.equals(getParameter(ACTION))) {
                Time timeStart = getParameterTime(TIME_START);
                Time timeEnd = getParameterTime(TIME_END);
                getServiceFactory().getWorkingDayService().save(userId, datesArray, timeStart, timeEnd);
            } else if (DELETE.equals(getParameter(ACTION))) {
                datesArray = removeDaysIfHaveAppointments(userId, datesArray);
                if (datesArray.length > 0) {
                    getServiceFactory().getWorkingDayService().deleteByUserIdAndDatesArray(userId, datesArray);
                }
            } else {
                sendError404();
            }
        } catch (ServiceException e) {
            sendError500();
        }
        redirect(SCHEDULE_REDIRECT + userId);
    }

    /**
     * Removes the days from the datesArray that have appointments.
     *
     * @param userId     the ID of the user whose schedule is being edited
     * @param datesArray an array of dates in the format "yyyy-MM-dd" to be checked for appointments
     * @return an array of dates in the format "yyyy-MM-dd" that do not have any appointments
     * @throws ServiceException if an error occurs while accessing the appointment service
     */
    private String[] removeDaysIfHaveAppointments(int userId, String[] datesArray) throws ServiceException {
        List<String> validDays = new ArrayList<>();
        List<String> removedDays = new ArrayList<>();
        for (String date : datesArray) {
            List<Appointment> appointments = serviceFactory.getAppointmentService().
                    getByDateAndMasterId(getLocalDate(date), userId);
            if (!appointments.isEmpty()) {
                removedDays.add(date);
            } else {
                validDays.add(date);
            }
        }
        if (!removedDays.isEmpty()) {
            setSessionAttribute(MESSAGE, MESSAGE_DELETE_DAYS);
            setSessionAttribute(REMOVED_DAYS, removedDays);
        }
        return validDays.toArray(new String[0]);
    }
}
