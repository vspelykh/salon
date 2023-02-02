package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;

import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.dao.mapper.Column.*;

public class FeedbackPostCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        Mark mark = new Mark();
        mark.setMark(Integer.parseInt(request.getParameter(MARK)));
        mark.setComment(request.getParameter(COMMENT));
        mark.setAppointmentId(Integer.valueOf(request.getParameter(APPOINTMENT_ID)));
        mark.setDate(LocalDateTime.now());
        try {
            getServiceFactory().getMarkService().save(mark);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        redirect(HOME_REDIRECT);
    }
}
