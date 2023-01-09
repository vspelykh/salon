package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.service.ConsultationService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.dao.Table.CONSULTATION;

public class ConsultationGetCommand extends Command {

    private ConsultationService consultationService = ServiceFactory.getConsultationService();

    @Override
    public void process() throws ServletException, IOException {
        try {
            request.setAttribute(CONSULTATION, consultationService.findAll());
            forward(CONSULTATION);
        } catch (ServiceException e) {
            //TODO
        }
    }
}
