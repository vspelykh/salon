package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

/**
 * This command is responsible for handling requests related to user roles.
 */
public class RolesCommand extends Command {

    /**
     * If the "search" parameter is not null, searches for users whose email contains the search string
     * and displays their roles in the view. Otherwise, simply displays the view.
     *
     * @throws ServletException if there is an error while processing the request
     * @throws IOException      if there is an error while sending the response
     */
    @Override
    public void process() throws ServletException, IOException {
        if (!isParameterNull(SEARCH)) {
            try {
                setRequestAttribute(MASTER, Role.HAIRDRESSER);
                setRequestAttribute(ADMIN, Role.ADMINISTRATOR);
                setRequestAttribute(CLIENT, Role.CLIENT);
                List<User> users = getServiceFactory().getUserService().findBySearch(getParameter(SEARCH));
                setRequestAttribute(USERS, users);
            } catch (ServiceException e) {
                sendError500();
            }
        }
        forward(ROLES);
    }
}
