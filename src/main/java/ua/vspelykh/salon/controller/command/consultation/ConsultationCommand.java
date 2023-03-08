package ua.vspelykh.salon.controller.command.consultation;

import ua.vspelykh.salon.controller.command.Command;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.command.CommandNames.CONSULTATION;

/**
 * The ConsultationCommand class represents a command to display the consultation page. It extends the Command class
 * and overrides the process method to forward the request to the consultation page.
 *
 * @version 1.0
 */
public class ConsultationCommand extends Command {

    /**
     * The process method of the ConsultationCommand class forwards the request to the consultation page.
     *
     * @throws ServletException if the servlet encounters difficulty while handling the request.
     * @throws IOException      if an input or output error is detected when the servlet handles the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        forward(CONSULTATION);
    }
}
