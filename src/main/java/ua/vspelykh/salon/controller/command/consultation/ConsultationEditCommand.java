package ua.vspelykh.salon.controller.command.consultation;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.ID;

/**
 * The ConsultationEditCommand class represents a command to edit a consultation. It extends the Command class and
 * overrides the process method to either delete or mark the consultation as read. If the "delete" action is specified
 * in the request parameters, the process method calls the delete method of the ConsultationService class to delete the
 * consultation with the ID parameter from the request. If the "read" action is specified, the method calls the findById
 * method of the ConsultationService class to retrieve the consultation with the ID parameter from the request, sets
 * its "read" property to true, and saves the consultation using the save method of the ConsultationService class. The
 * process method then redirects the request to either the home page or the consultation page, depending on the value of
 * the "redirect" request parameter.
 *
 * @version 1.0
 **/
public class ConsultationEditCommand extends Command {

    private static final String DELETE = "delete";
    private static final String READ = "read";

    /**
     * Deletes or marks a consultation as read based on the request parameters. If the "delete" action is specified, the
     * consultation with the ID parameter from the request is deleted using the delete method of the ConsultationService
     * class. If the "read" action is specified, the consultation with the ID parameter from the request is retrieved
     * using the findById method of the ConsultationService class, its "read" property is set to true, and the consultation
     * is saved using the save method of the ConsultationService class. The method then redirects the request to either
     * the home page or the consultation page, depending on the value of the "redirect" request parameter.
     *
     * @throws ServletException if the servlet encounters difficulty while handling the request.
     * @throws IOException      if an input or output error is detected when the servlet handles the request.
     */
    @Override
    public void process() throws ServletException, IOException {
        try {
            Integer consultationId = getParameterInt(ID);
            if (DELETE.equals(getParameter(ACTION))) {
                getServiceFactory().getConsultationService().delete(consultationId);

            } else if (READ.equals(getParameter(ACTION))) {
                Consultation consultation = getServiceFactory().getConsultationService().findById(consultationId);
                consultation.setRead(true);
                getServiceFactory().getConsultationService().save(consultation);
            }
            if (REDIRECT.equals(getParameter(REDIRECT))) {
                redirect(HOME_REDIRECT);
            } else {
                redirect(CONSULTATION_REDIRECT);
            }
        } catch (ServiceException e) {
            sendError500();
        }
    }
}
