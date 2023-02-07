package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.Consultation;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.SUCCESS_REDIRECT;
import static ua.vspelykh.salon.dao.mapper.Column.NAME;
import static ua.vspelykh.salon.dao.mapper.Column.NUMBER;

public class ConsultationPostCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        Consultation consultation = new Consultation(null, request.getParameter(NAME), request.getParameter(NUMBER));
        try {
            getServiceFactory().getConsultationService().save(consultation);
        } catch (ServiceException e) {
            response.sendError(500);
        }
        redirect(context.getContextPath() + SUCCESS_REDIRECT);
    }
}
