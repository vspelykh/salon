package ua.vspelykh.salon.controller.command.response;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

/**
 * Represents an ErrorCommand, a subclass of Command that processes errors.
 *
 * @version 1.0
 */
public class ErrorCommand extends Command {

    /**
     * Processes the error request by checking if the "MESSAGE" attribute is not null.
     * If it's not null, the method forwards the request to the "ERROR_FORWARD" page.
     * Otherwise, it redirects the request to the "HOME_REDIRECT" page.
     *
     * @throws ServletException if there's an error with the servlet processing.
     * @throws IOException      if there's an input/output error.
     */
    @Override
    public void process() throws ServletException, IOException {
        if (request.getSession().getAttribute(MESSAGE) != null) {
            forward(ERROR_FORWARD);
        } else redirect(HOME_REDIRECT);
    }
}
