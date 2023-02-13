package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class RolesCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        String search = request.getParameter(SEARCH);
        if (checkNullParam(search)) {
            try {
                request.setAttribute("master", Role.HAIRDRESSER);
                request.setAttribute("admin", Role.ADMINISTRATOR);
                request.setAttribute("client", Role.CLIENT);
                List<User> users = getServiceFactory().getUserService().findBySearch(search);
                request.setAttribute(USERS, users);
            } catch (ServiceException e) {
                response.sendError(500);
            }
        }
        forward(ROLES);
    }
}
