package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.command.CommandNames.INVITATION;

/**
 * The InvitationFormCommand class extends the abstract Command class and is responsible for processing the
 * invitation form page requests.
 *
 * @version 1.0
 */
public class InvitationFormCommand extends Command {

    /**
     * This method is responsible for processing the invitation form page request.
     * It simply forwards the request to the invitation form page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        forward(INVITATION);
    }
}
