package ua.vspelykh.salon.controller.command.appointment;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.CONSULTATION_REDIRECT;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

/**
 * The ConsultationDeleteCommand class represents a command to delete a consultation. It extends the Command class and
 * overrides the process method to delete the consultation by calling the delete method of the ConsultationService class,
 * passing the ID parameter from the request. It then redirects the request to the consultation page.
 *
 * @version 1.0
 **/
public class ConsultationDeleteCommand extends Command {

    /**
     * The process method of the ConsultationDeleteCommand class deletes the consultation by calling the delete method
     * of the ConsultationService class, passing the ID parameter from the request. It then redirects the request
     * to the consultation page.
     *
     * @throws ServletException if the servlet encounters difficulty while handling the request.
     * @throws IOException      if an input or output error is detected when the servlet handles the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            getServiceFactory().getConsultationService().delete(getParameterInt(ID));
            redirect(CONSULTATION_REDIRECT);
        } catch (ServiceException e) {
            sendError500();
        }
    }
}
