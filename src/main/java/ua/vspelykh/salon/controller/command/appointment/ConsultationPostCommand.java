package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.SUCCESS_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.NAME;
import static ua.vspelykh.salon.model.dao.mapper.Column.NUMBER;

/**
 * A Command implementation for handling POST requests to create a new consultation.
 *
 * @version 1.0
 */
public class ConsultationPostCommand extends Command {

    /**
     * Processes the POST request, creates a new consultation object using the request parameters,
     * and saves it using the ConsultationService. Upon success, redirects the user to a success page.
     *
     * @throws ServletException if there is an error in the servlet or its configuration
     * @throws IOException      if an input or output error is detected when the servlet handles the POST request
     */
    @Override
    public void process() throws ServletException, IOException {
        Consultation consultation = Consultation.builder().
                name(getParameter(NAME)).
                number(getParameter(NUMBER)).build();
        try {
            getServiceFactory().getConsultationService().save(consultation);
        } catch (ServiceException e) {
            sendError500();
        }
        redirect(SUCCESS_REDIRECT);
    }
}
