package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;

import static ua.vspelykh.salon.controller.ControllerConstants.MESSAGE;
import static ua.vspelykh.salon.controller.ControllerConstants.SUCCESS_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_FEEDBACK;

/**
 * The FeedbackPostCommand class extends the Command class and handles the POST request for creating a new feedback.
 *
 * @version 1.0
 */
public class FeedbackPostCommand extends Command {

    /**
     * This method is responsible for processing the request by creating a Feedback object based on the form data
     * submitted in the request, and then saving the feedback to the feedback service using the service factory.
     * If the service throws a ServiceException, the method sends a 500 error response.
     * Finally, it sets a success message in the session and redirects to the success page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        Feedback feedback = Feedback.builder().mark(getParameterInt(MARK))
                .comment(getParameter(COMMENT))
                .appointmentId(getParameterInt(APPOINTMENT_ID))
                .date(LocalDateTime.now())
                .build();
        try {
            getServiceFactory().getFeedbackService().save(feedback);
        } catch (ServiceException e) {
            sendError500();
        }
        setSessionAttribute(MESSAGE, MESSAGE_FEEDBACK);
        redirect(SUCCESS_REDIRECT);
    }
}
