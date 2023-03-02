package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashSet;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

/**
 * The LogoutCommand class extends the abstract Command class and is responsible for logging out the user from the application.
 * It removes the user's session attributes and sets a default guest user with 'GUEST' role in the session.
 *
 * @version 1.0
 */
public class LogoutCommand extends Command {

    /**
     * This method is responsible for logging out the user from the application.
     * It removes the 'IS_LOGGED' and 'CURRENT_USER' session attributes.
     * It sets a default guest user with 'GUEST' role in the session.
     * <p>
     * It then redirects the user to the home page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        request.getSession().removeAttribute(IS_LOGGED);
        request.getSession().removeAttribute(CURRENT_USER);
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.GUEST);
        User guestUser = User.builder().roles(roles).build();
        setSessionAttribute(CURRENT_USER, guestUser);
        redirect(HOME_REDIRECT);
    }
}