package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class CheckLoginCommand extends Command {

    private UserService userService = ServiceFactory.getUserService();

    private String login;
    private String password;

    @Override
    public void process() throws ServletException, IOException {
        login = request.getParameter(LOGIN);
        password = request.getParameter(PASSWORD);

        if (login != null && password != null) {
            try {
                User user = userService.findByEmailAndPassword(login, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute(CURRENT_USER, user);
                    session.setAttribute(ROLES, user.getRoles());
                    session.setAttribute(IS_LOGGED, true);
                    response.sendRedirect(context.getContextPath() + HOME_REDIRECT);
                }
            } catch (ServiceException e) {
                request.setAttribute(MESSAGE, MESSAGE_INCORRECT_LOGIN_PASSWORD);
                request.setAttribute(INS_LOGIN, login);
                request.setAttribute(INS_PASSWORD, password);
                forward(LOGIN);
            }
        }
    }
}
