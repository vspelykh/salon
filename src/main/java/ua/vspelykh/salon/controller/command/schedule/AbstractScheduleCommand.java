package ua.vspelykh.salon.controller.command.schedule;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

/**
 * The AbstractScheduleCommand class is an abstract class that provides common functionality for schedule-related commands.
 * It extends the Command abstract class and provides constants for action types, time start and time end parameters.
 * <p>
 * It also provides a helper method for setting the current working days for a user in the request attribute.
 *
 * @version 1.0
 */
public abstract class AbstractScheduleCommand extends Command {

    protected static final String ACTION = "action";
    protected static final String SAVE = "save";
    protected static final String DELETE = "delete";
    protected static final String TIME_START = "time-start";
    protected static final String TIME_END = "time-end";

    /**
     * This method sets the current working days for a user in the request attribute.
     * It calls the WorkingDayService to retrieve the working days for the user with the ID parameter.
     * If there are no working days for the user, it sets an empty string in the DAYS attribute.
     * If there are working days for the user, it builds a string of formatted dates and sets it in the DAYS attribute.
     *
     * @throws ServiceException if the service layer throws an exception
     */
    protected void setCurrentWorkingDays() throws ServiceException {
        List<WorkingDay> workingDays = serviceFactory.getWorkingDayService().findByUserId(getParameterInt(ID));
        if (workingDays.isEmpty()) {
            setRequestAttribute(DAYS, EMPTY_STRING);
        } else {
            StringBuilder builder = new StringBuilder();
            for (WorkingDay workingDay : workingDays) {
                builder.append("\"").append(workingDay.getDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                        .append("\"").append(", ");
            }
            builder.replace(builder.length() - 2, builder.length(), EMPTY_STRING);
            setRequestAttribute(DAYS, builder.toString());
        }
    }
}
