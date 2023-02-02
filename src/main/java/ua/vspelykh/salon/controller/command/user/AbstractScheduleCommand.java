package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.dao.mapper.Column.ID;

public abstract class AbstractScheduleCommand extends Command {

    protected static final String ACTION = "action";
    protected static final String SAVE = "save";
    protected static final String DELETE = "delete";
    protected static final String TIME_START = "time-start";
    protected static final String TIME_END = "time-end";


    protected void setCurrentWorkingDays() throws ServiceException {
        List<WorkingDay> workingDays = serviceFactory.getWorkingDayService().
                findDaysByUserId(Integer.valueOf(request.getParameter(ID)));
        if (workingDays.isEmpty()) {
            request.setAttribute(DAYS, "");
        } else {
            StringBuilder builder = new StringBuilder();
            for (WorkingDay workingDay : workingDays) {
                builder.append("\"").append(workingDay.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .append("\"").append(", ");
            }
            builder.replace(builder.length() - 2, builder.length(), "");
            request.setAttribute(DAYS, builder.toString());
        }
    }
}
