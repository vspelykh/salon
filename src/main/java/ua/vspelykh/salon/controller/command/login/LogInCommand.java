package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.controller.command.CommandNames;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * The LogInCommand class extends the abstract Command class and is responsible for processing login page requests.
 *
 * @version 1.0
 */
public class LogInCommand extends Command {

    /**
     * This method is responsible for processing the login page request.
     * It forwards the request to the login page defined in the CommandNames class.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        forward(CommandNames.LOGIN);
    }
}
