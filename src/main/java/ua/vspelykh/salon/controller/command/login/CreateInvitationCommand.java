package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.MESSAGE;
import static ua.vspelykh.salon.controller.ControllerConstants.SUCCESS_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.EMAIL;
import static ua.vspelykh.salon.model.dao.mapper.Column.ROLE;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_SUCCESS_INVITATION;

/**
 * The CreateInvitationCommand class extends the abstract Command class and is responsible for processing requests
 * to create new invitations.
 *
 * @version 1.0
 */
public class CreateInvitationCommand extends Command {

    /**
     * This method is responsible for processing the request to create a new invitation.
     * It retrieves the email and role parameters from the request and passes them to the service layer to create
     * a new invitation. If any exception occurs, it sends a 500 error response.
     * Once the invitation is successfully created, it sets a success message in the session attribute and redirects
     * to the success page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            getServiceFactory().getInvitationService().create(getParameter(EMAIL), Role.valueOf(getParameter(ROLE)));
        } catch (ServiceException e) {
            sendError500();
        }
        setSessionAttribute(MESSAGE, MESSAGE_SUCCESS_INVITATION);
        redirect(SUCCESS_REDIRECT);
    }
}
