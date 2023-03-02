package ua.vspelykh.salon.controller.command.user;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.command.CommandNames.ADMIN;

/**
 * The AdminCommand class extends the abstract Command class and is responsible for processing requests to access
 * the admin panel of the application.
 *
 * @version 1.0
 */
public class AdminCommand extends Command {

    /**
     * This method processes the request to access the admin panel of the application.
     * It forwards the request to the admin view.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        forward(ADMIN);
    }
}
