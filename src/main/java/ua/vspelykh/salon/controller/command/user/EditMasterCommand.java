package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.SCHEDULE_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

public class EditMasterCommand extends Command {
    @Override
    public void process() throws ServletException, IOException {
        try {
            int masterId = Integer.parseInt(request.getParameter(ID));
            MastersLevel level = MastersLevel.valueOf(request.getParameter(LEVEL));
            String about = request.getParameter(ABOUT);
            String aboutUa = String.valueOf(request.getParameter(ABOUT + UA));
            boolean active = Boolean.parseBoolean(request.getParameter(ACTIVE));
            UserLevel userLevel = new UserLevel(masterId, level, about, aboutUa, active);
            serviceFactory.getUserService().update(userLevel);
            redirect(SCHEDULE_REDIRECT + masterId);
        } catch (ServiceException e) {
            response.sendError(500);
        }
    }
}
