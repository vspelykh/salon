package ua.vspelykh.salon.controller.command.consultation;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.NAME;
import static ua.vspelykh.salon.model.dao.mapper.Column.NUMBER;
import static ua.vspelykh.salon.util.exception.Messages.CONSULTATION_ERROR;
import static ua.vspelykh.salon.util.exception.Messages.CONSULTATION_SUCCESS;

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
            setSessionAttribute(MESSAGE, CONSULTATION_SUCCESS);
            redirect(SUCCESS_REDIRECT);
        } catch (ServiceException e) {
            setSessionAttribute(MESSAGE, CONSULTATION_ERROR);
            redirect(ERROR_REDIRECT);
        }
    }
}
