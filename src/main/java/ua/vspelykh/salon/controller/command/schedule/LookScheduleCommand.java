package ua.vspelykh.salon.controller.command.schedule;

import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.USER;
import static ua.vspelykh.salon.controller.command.CommandNames.LOOK_SCHEDULE;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

/**
 * Command for displaying the schedule of a specific user (master).
 * Extends AbstractScheduleCommand to reuse methods for handling schedule data.
 * Sets the current working days and the user attribute in the request and forwards to the Look Schedule view.
 *
 * @version 1.0
 */
public class LookScheduleCommand extends AbstractScheduleCommand {

    /**
     * Retrieves the schedule data of a specific user (master), sets the current working days and the user attribute
     * in the request, and forwards to the Look Schedule view.
     * If an error occurs, sends a 404 error response.
     *
     * @throws ServletException If there is a servlet-related problem
     * @throws IOException      If there is an I/O problem
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setCurrentWorkingDays();
            setRequestAttribute(USER, getServiceFactory().getUserService().findById(getParameterInt(ID)));
        } catch (ServiceException e) {
            sendError404();
        }
        forward(LOOK_SCHEDULE);
    }
}
