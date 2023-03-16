package ua.vspelykh.salon.controller.command.consultation;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.model.dao.Table.CONSULTATION;

/**
 * This class is a Command class that handles the GET request for the consultation page.
 *
 * @version 1.0
 */
public class ConsultationGetCommand extends Command {

    /**
     * Handles the GET request for the consultation page. Sets the consultations attribute to the list of
     * all consultations using the ConsultationService findAll method and forwards the request to the
     * consultations JSP page.
     *
     * @throws ServletException if there is an exception while processing the request
     * @throws IOException      if there is an I/O exception while processing the request
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            setRequestAttribute(CONSULTATION, serviceFactory.getConsultationService().findAll());
            forward(CONSULTATION);
        } catch (ServiceException e) {
            sendError500();
        }
    }
}
