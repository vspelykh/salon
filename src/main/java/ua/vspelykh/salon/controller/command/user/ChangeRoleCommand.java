package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ROLE;
import static ua.vspelykh.salon.model.dao.mapper.Column.USER_ID;

/**
 * The ChangeRoleCommand class extends the abstract Command class and is responsible for handling the change of a user's role.
 * It retrieves the user ID, the new role, and the action (either 'save' or 'delete') from the request parameters.
 *
 * @version 1.0
 */
public class ChangeRoleCommand extends Command {

    /**
     * This method handles the change of a user's role.
     * It retrieves the user ID, the new role, and the action (either 'save' or 'delete') from the request parameters.
     * It then updates the user's role in the database, sets a success message in the session, and redirects to the
     * 'roles' page with a search parameter.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        Role role = Role.valueOf(getParameter(ROLE));
        String action = getParameter(ACTION);
        int userId = getParameterInt(USER_ID);
        try {
            getServiceFactory().getUserService().updateRole(userId, action, role);
            User user = getServiceFactory().getUserService().findById(userId);
            setSessionAttribute(MESSAGE, SUCCESS);
            redirect(ROLES_REDIRECT + user.getEmail());
        } catch (ServiceException e) {
            sendError500();
        }
    }
}
