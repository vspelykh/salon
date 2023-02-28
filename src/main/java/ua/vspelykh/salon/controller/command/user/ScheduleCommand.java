package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.USER;
import static ua.vspelykh.salon.controller.ControllerConstants.USER_LEVEL;
import static ua.vspelykh.salon.controller.command.CommandNames.SCHEDULE;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

public class ScheduleCommand extends AbstractScheduleCommand {

    @Override
    public void process() throws ServletException, IOException {
        try {
            setCurrentWorkingDays();
            int masterId = Integer.parseInt(request.getParameter(ID));
            request.setAttribute(USER, getServiceFactory().getUserService().findById(masterId));
            UserLevel userLevel = serviceFactory.getUserService().getUserLevelByUserId(masterId);
            request.setAttribute(USER_LEVEL, userLevel);
        } catch (ServiceException e) {
            response.sendError(404);
        }
        forward(SCHEDULE);
    }
}
