package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class CheckLoginCommand extends Command {

    @Override
    public void process() throws ServletException, IOException {
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);

        if (login != null && password != null) {
            try {
                User user = getServiceFactory().getUserService().findByEmailAndPassword(login, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(CURRENT_USER, user);
                    session.setAttribute(IS_LOGGED, true);
                    if (session.getAttribute(LAST_PAGE) != null) {
                        String path = (String) session.getAttribute(LAST_PAGE);
                        session.removeAttribute(LAST_PAGE);
                        redirect(path);
                    } else {
                        redirect(context.getContextPath() + HOME_REDIRECT);
                    }
                }
            } catch (ServiceException e) {
                request.getSession().setAttribute(MESSAGE, MESSAGE_INCORRECT_LOGIN_PASSWORD);
                request.setAttribute(INS_LOGIN, login);
                request.setAttribute(INS_PASSWORD, password);
                redirect(HOME_REDIRECT + COMMAND_PARAM + LOGIN);
            }
        } else {
            response.sendError(500);
        }
    }
}
