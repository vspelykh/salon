package ua.vspelykh.salon.controller.command.response;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

/**
 * The SuccessCommand class extends the abstract Command class and is responsible for processing the success page requests.
 *
 * @version 1.0
 */
public class SuccessCommand extends Command {

    /**
     * This method checks if there is a success message in the session attribute.
     * If yes, it forwards the request to the success page.
     * Otherwise, it redirects the request to the home page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        if (request.getSession().getAttribute(MESSAGE) != null) {
            forward(SUCCESS);
        } else redirect(HOME_REDIRECT);
    }
}
