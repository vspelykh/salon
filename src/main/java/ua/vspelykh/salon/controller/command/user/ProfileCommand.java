package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.ControllerConstants;
import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Set;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class ProfileCommand extends Command {

    private UserService userService = ServiceFactory.getUserService();

    @Override
    public void process() throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        request.setAttribute(ControllerConstants.USER, user);
        Set<Role> roles = (Set<Role>) request.getSession().getAttribute("roles");
        request.setAttribute(ROLES, roles);
        request.setAttribute(IS_MASTER, roles.contains(Role.HAIRDRESSER));
        request.setAttribute(IS_ADMIN, roles.contains(Role.ADMINISTRATOR));
        request.setAttribute(IS_CLIENT, roles.contains(Role.CLIENT));
        if (roles.contains(Role.HAIRDRESSER)){
            try {
                request.setAttribute(USER_LEVEL, userService.getUserLevelByUserId(user.getId()));
            } catch (ServiceException e) {
                //TODO
            }
        }
        forward(PROFILE);
    }


}
