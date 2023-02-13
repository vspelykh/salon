package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.model.dao.Table.CONSULTATION;

public class ConsultationGetCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            request.setAttribute(CONSULTATION, getServiceFactory().getConsultationService().findAll());
            forward(CONSULTATION);
        } catch (ServiceException e) {
            response.sendError(500);
        }
    }
}
