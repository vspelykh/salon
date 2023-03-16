package ua.vspelykh.salon.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.ConsultationService;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.impl.ServiceFactoryImpl;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.CURRENT_USER;
import static ua.vspelykh.salon.listener.ListenerConstants.ERROR_CLOSE_CONNECTION;
import static ua.vspelykh.salon.listener.ListenerConstants.ERROR_NOTIFICATION;

/**
 * This class is a servlet request listener that is responsible for adding a list of new consultations to the
 * request attribute for administrators to view in the header of the application.
 *
 * @version 1.0
 */
public class NotificationListener implements ServletRequestListener {

    private static final Logger LOG = LogManager.getLogger(NotificationListener.class);
    private static final String NEW_CONSULTATIONS = "new_consultations";

    private ServiceFactory serviceFactory;
    private ConsultationService consultationService;

    /**
     * Initializes the NotificationListener and adds the list of new consultations to the
     * request attribute if the current user is an administrator.
     *
     * @param sre the servlet request event
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        if (currentUser != null && currentUser.getRoles().contains(Role.ADMINISTRATOR)) {
            try {
                serviceFactory = ServiceFactoryImpl.getServiceFactory();
                consultationService = serviceFactory.getConsultationService();
            } catch (ServiceException e) {
                LOG.error(ERROR_NOTIFICATION);
            }
            try {
                addNewConsultationsToRequest(request);
            } catch (ServiceException e) {
                LOG.error(ERROR_NOTIFICATION);
            }
        }
    }

    /**
     * Destroys the and closes the service factory connection.
     *
     * @param sre the servlet request event
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        try {
            if (serviceFactory != null) {
                serviceFactory.close();
            }
        } catch (Exception e) {
            LOG.error(ERROR_CLOSE_CONNECTION);
        }
    }

    /**
     * Gets a list of new consultations from the consultation service and adds it to the request
     * attribute if it's not empty.
     *
     * @param request the servlet request
     * @throws ServiceException if there's an error getting the new consultations
     */
    private void addNewConsultationsToRequest(HttpServletRequest request) throws ServiceException {
        List<Consultation> newConsultations = consultationService.getNewConsultations();
        if (!newConsultations.isEmpty()) {
            request.setAttribute(NEW_CONSULTATIONS, newConsultations);
        }
    }
}
