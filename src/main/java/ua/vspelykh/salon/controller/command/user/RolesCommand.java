package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class RolesCommand extends Command {

    private UserService userService = ServiceFactory.getUserService();

    @Override
    public void process() throws ServletException, IOException {
        String search = request.getParameter(SEARCH);
        if (checkNullParam(search)) {
            try {
                request.setAttribute("master", Role.HAIRDRESSER);
                request.setAttribute("admin", Role.ADMINISTRATOR);
                request.setAttribute("client", Role.CLIENT);
                List<User> users = userService.findBySearch(search);
                request.setAttribute(USERS, users);
            } catch (ServiceException e) {
                //TODO
            }
        }
        forward(ROLES);
    }
}
