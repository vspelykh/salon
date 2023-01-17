package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.DAYS;
import static ua.vspelykh.salon.controller.ControllerConstants.USER;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.dao.mapper.Column.ID;

public class ScheduleCommand extends Command {

    private UserService userService = ServiceFactory.getUserService();
    private WorkingDayService workingDayService = ServiceFactory.getWorkingDayService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            setCurrentWorkingDays();
            request.setAttribute(USER, userService.findById(Integer.valueOf(request.getParameter(ID))));
        } catch (ServiceException e) {
            //todo
        }
        forward(SCHEDULE);
    }

    private void setCurrentWorkingDays() throws ServiceException {
        List<WorkingDay> workingDays = workingDayService.findDaysByUserId(Integer.valueOf(request.getParameter(ID)));
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
