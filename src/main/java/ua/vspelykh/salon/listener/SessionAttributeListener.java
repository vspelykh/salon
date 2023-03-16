package ua.vspelykh.salon.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.impl.ServiceFactoryImpl;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static ua.vspelykh.salon.controller.ControllerConstants.CURRENT_USER;
import static ua.vspelykh.salon.controller.ControllerConstants.IS_LOGGED;
import static ua.vspelykh.salon.listener.ListenerConstants.ERROR_ATTR;
import static ua.vspelykh.salon.listener.ListenerConstants.ERROR_CLOSE_CONNECTION;

/**
 * Class that updates the session attributes for the currently logged-in user
 * based on the user information in the database.
 *
 * @version 1.0
 */
public class SessionAttributeListener implements ServletRequestListener {

    private static final Logger LOG = LogManager.getLogger(SessionAttributeListener.class);

    private ServiceFactory serviceFactory;

    /**
     * Initializes the session attributes for the currently logged-in user based on the user information
     * in the database.
     *
     * @param sre the ServletRequestEvent containing the servlet request
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        if (isLogged(session)) {
            try {
                serviceFactory = ServiceFactoryImpl.getServiceFactory();
                User currentUser = (User) session.getAttribute(CURRENT_USER);
                User userInDB = getUserFromDatabase(currentUser.getId());
                if (!currentUser.equals(userInDB)) {
                    session.setAttribute(CURRENT_USER, userInDB);
                }
            } catch (ServiceException e) {
                LOG.error(ERROR_ATTR);
            }
        }
    }

    /**
     * Cleans up the resources used by this listener.
     *
     * @param sre the {@link ServletRequestEvent} containing the servlet request
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        if (serviceFactory != null) {
            try {
                serviceFactory.close();
            } catch (Exception e) {
                LOG.error(ERROR_CLOSE_CONNECTION);
            }
        }
    }

    /**
     * Checks whether the user is currently logged in to the session.
     *
     * @param session the HTTP session
     * @return true if the user is logged in, false otherwise
     */
    private boolean isLogged(HttpSession session) {
        return session.getAttribute(IS_LOGGED) != null && (boolean) session.getAttribute(IS_LOGGED)
                && session.getAttribute(CURRENT_USER) != null;
    }

    /**
     * Retrieves the user information from the database.
     *
     * @param id the ID of the user
     * @return the {@link User} object representing the user information
     * @throws ServiceException if there is an error retrieving the user information from the database
     */
    private User getUserFromDatabase(int id) throws ServiceException {
        return serviceFactory.getUserService().findById(id);
    }
}
