package ua.vspelykh.salon.controller.command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.HOME_PAGE;

/**
 * The HomeCommand class is a concrete implementation of the Command pattern that processes the HOME command
 * by forwarding the request to the home page view.
 *
 * @version 1.0
 */
public class HomeCommand extends Command {

    /**
     * Processes the HOME command by forwarding the request to the HOME_PAGE view.
     *
     * @throws ServletException if an error occurs during the processing of the request.
     * @throws IOException      if an I/O error occurs while processing the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        forward(HOME_PAGE);
    }
}
