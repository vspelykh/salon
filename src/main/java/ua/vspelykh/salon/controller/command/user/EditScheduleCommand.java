package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Time;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

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
     * It retrieves the selected days and time range from the request, and saves them to the database if the action is 'SAVE'.
     * If the action is 'DELETE', it retrieves the selected days from the request and deletes them from the database.
     * <p>
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
                getServiceFactory().getWorkingDayService().deleteWorkingDaysByUserIdAndDatesArray(userId, datesArray);
            } else {
                sendError404();
            }
        } catch (ServiceException e) {
            sendError500();
        }
        redirect(SCHEDULE_REDIRECT + userId);
    }
}
