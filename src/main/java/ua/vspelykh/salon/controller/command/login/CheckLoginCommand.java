package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

/**
 * The CheckLoginCommand class extends the abstract Command class and is responsible for processing login requests.
 *
 * @version 1.0
 */
public class CheckLoginCommand extends Command {

    /**
     * This method is responsible for processing the login request. It retrieves the login and password parameters
     * from the request, validates them against the user database, and sets the current user session attribute
     * if the login is successful. It also redirects the user to the last visited page or the homepage,
     * depending on whether there is a last page session attribute.
     * <p>
     * If an incorrect login or password is entered, the method sets the appropriate message attribute and redirects
     * the user to the login page. If any exception occurs, it sends a 500 error response.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        String login = getParameter(LOGIN);
        String password = getParameter(PASSWORD);

        if (login != null && password != null) {
            try {
                User user = getServiceFactory().getUserService().findByEmailAndPassword(login, password);
                if (user != null) {
                    setSessionAttribute(CURRENT_USER, user);
                    setSessionAttribute(IS_LOGGED, true);
                    HttpSession session = request.getSession();
                    if (session.getAttribute(LAST_PAGE) != null) {
                        String path = (String) session.getAttribute(LAST_PAGE);
                        session.removeAttribute(LAST_PAGE);
                        redirect(path);
                    } else {
                        redirect(HOME_REDIRECT);
                    }
                }
            } catch (ServiceException e) {
                request.getSession().setAttribute(MESSAGE, MESSAGE_INCORRECT_LOGIN_PASSWORD);
                setRequestAttribute(INS_LOGIN, login);
                setRequestAttribute(INS_PASSWORD, password);
                redirect(LOGIN_REDIRECT);
            }
        } else {
            sendError500();
        }
    }
}
