package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.USER;
import static ua.vspelykh.salon.controller.ControllerConstants.USER_LEVEL;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

/**
 * This command is responsible for displaying the schedule of a hairdresser. It extends the AbstractScheduleCommand
 * class that contains common methods used for manipulating the schedule.
 */
public class ScheduleCommand extends AbstractScheduleCommand {

    /**
     * Processes the request by setting the current working days, retrieving the user and user level objects,
     * and forwarding the request to the schedule page. If any service exception occurs, it sends an HTTP error
     * 404 response.
     *
     * @throws ServletException if any servlet exception occurs
     * @throws IOException      if any I/O exception occurs
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setCurrentWorkingDays();
            int masterId = getParameterInt(ID);
            setRequestAttribute(USER, getServiceFactory().getUserService().findById(masterId));
            UserLevel userLevel = serviceFactory.getUserService().getUserLevelByUserId(masterId);
            setRequestAttribute(USER_LEVEL, userLevel);
        } catch (ServiceException e) {
            sendError404();
        }
        forward(SCHEDULE);
    }
}
