package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.COMMAND_PARAM;
import static ua.vspelykh.salon.controller.ControllerConstants.HOME_REDIRECT;
import static ua.vspelykh.salon.dao.Table.CONSULTATION;
import static ua.vspelykh.salon.dao.mapper.Column.ID;

public class ConsultationDeleteCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        try {
            getServiceFactory().getConsultationService().delete(Integer.valueOf(request.getParameter(ID)));
            redirect(context.getContextPath() + HOME_REDIRECT + COMMAND_PARAM + CONSULTATION);
        } catch (ServiceException e) {
            //TODO
        }
    }
}
