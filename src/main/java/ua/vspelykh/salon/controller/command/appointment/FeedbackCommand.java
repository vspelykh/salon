package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.FEEDBACK;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

public class FeedbackCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            checkIsRequestValid();
        } catch (Exception e) {
            response.sendError(404);
        }
    }

    private void checkIsRequestValid() throws IOException, ServiceException, ServletException {
        if (request.getParameter(ID) == null || request.getParameter(ID).isEmpty()) {
            response.sendError(403);
            return;
        }
        Integer appointmentId = Integer.valueOf(request.getParameter(ID));
        Appointment appointment = serviceFactory.getAppointmentService().findById(appointmentId);
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        Feedback mark = getServiceFactory().getFeedbackService().getFeedbackByAppointmentId(appointmentId);
        if (mark != null){
            request.getSession().setAttribute(MESSAGE, MESSAGE + DOT + FEEDBACK + ".exist");
            redirect(SUCCESS_REDIRECT);
            return;
        }
        if (!appointment.getClientId().equals(currentUser.getId()) || appointment.getStatus() != AppointmentStatus.SUCCESS){
            response.sendError(403);
            return;
        }
        forward(FEEDBACK);
    }
}
