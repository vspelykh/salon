package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.MESSAGE;
import static ua.vspelykh.salon.controller.ControllerConstants.SUCCESS_REDIRECT;
import static ua.vspelykh.salon.controller.command.CommandNames.FEEDBACK;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;
import static ua.vspelykh.salon.util.exception.Messages.MESSAGE_FEEDBACK_EXISTS;

/**
 * The FeedbackCommand class extends the Command class and is responsible for checking whether a client is authorized to
 * give feedback for a completed appointment.
 *
 * @version 1.0
 */
public class FeedbackCommand extends Command {

    /**
     * This method is responsible for processing the request and checking the validity of the request. If the request is
     * invalid, the user is redirected to the error page. Otherwise, it checks whether feedback for the given appointment
     * already exists. If the feedback exists, the user is redirected to the success page with an error message. Otherwise,
     * it checks whether the client is authorized to give feedback for the appointment. If the client is authorized, the user
     * is forwarded to the feedback page. Otherwise, the user is redirected to the error page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            if (isParameterNull(ID)) {
                sendError403();
                return;
            }
            Integer appointmentId = getParameterInt(ID);
            Appointment appointment = serviceFactory.getAppointmentService().findById(appointmentId);
            if (isFeedbackExists(appointment)) {
                setSessionAttribute(MESSAGE, MESSAGE_FEEDBACK_EXISTS);
                redirect(SUCCESS_REDIRECT);
                return;
            }
            if (!isAuthorizedToGiveFeedback(appointment, getCurrentUser())) {
                sendError403();
                return;
            }
            forward(FEEDBACK);
        } catch (Exception e) {
            sendError404();
        }
    }

    /**
     * This method checks whether feedback for the given appointment already exists.
     *
     * @param appointment the appointment for which to check feedback existence
     * @return true if feedback exists for the appointment, false otherwise
     * @throws ServiceException if an error occurs while retrieving feedback information
     */
    private boolean isFeedbackExists(Appointment appointment) throws ServiceException {
        Feedback feedback = getServiceFactory().getFeedbackService().getFeedbackByAppointmentId(appointment.getId());
        return feedback != null;
    }

    /**
     * This method checks whether the client is authorized to give feedback for the appointment.
     *
     * @param appointment the appointment for which to check authorization
     * @param currentUser the user who is trying to give feedback
     * @return true if the client is authorized to give feedback for the appointment, false otherwise
     */
    private boolean isAuthorizedToGiveFeedback(Appointment appointment, User currentUser) {
        return appointment.getClientId().equals(currentUser.getId()) && appointment.getStatus() == AppointmentStatus.SUCCESS;
    }
}
