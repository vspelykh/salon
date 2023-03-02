package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Set;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

/**
 * Command for displaying user profile page.
 * <p>
 * This command retrieves the current user from the session and sets request attributes for the user's
 * roles and user level (if the user is a hairdresser).
 */
public class ProfileCommand extends Command {

    /**
     * Processes the request and sets the necessary request attributes for the user profile page.
     *
     * @throws ServletException If there is a servlet-related problem
     * @throws IOException      If there is an I/O problem
     */
    @Override
    public void process() throws ServletException, IOException {
        User user = getCurrentUser();
        request.setAttribute(USER, user);
        Set<Role> roles = user.getRoles();
        setRequestAttribute(ROLES, roles);
        setRequestAttribute(IS_MASTER, roles.contains(Role.HAIRDRESSER));
        setRequestAttribute(IS_ADMIN, roles.contains(Role.ADMINISTRATOR));
        setRequestAttribute(IS_CLIENT, roles.contains(Role.CLIENT));
        if (roles.contains(Role.HAIRDRESSER)) {
            try {
                setRequestAttribute(USER_LEVEL, getServiceFactory().getUserService().getUserLevelByUserId(user.getId()));
            } catch (ServiceException e) {
                sendError500();
            }
        }
        forward(PROFILE);
    }
}
