package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.controller.command.CommandNames.REGISTRATION;
import static ua.vspelykh.salon.dao.mapper.Column.KEY;

public class RegistrationCommand extends Command {

    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String EMAIL = "email";
    private static final String NUMBER = "number";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_REPEAT = "passwordRepeat";

    private String message;

    @Override
    public void process() throws ServletException, IOException {
        User user = checkFieldsAndBuildUser();
        if (user != null) {
            try {
                if (request.getParameter(KEY) != null && !request.getParameter(KEY).isEmpty()) {
                    getServiceFactory().getUserService().save(user, request.getParameter(KEY));
                } else {
                    getServiceFactory().getUserService().save(user);
                }
                request.getSession().setAttribute(MESSAGE, REGISTRATION + DOT + SUCCESS);
                redirect(context.getContextPath() + SUCCESS_REDIRECT);
            } catch (ServiceException e) {
                request.setAttribute(MESSAGE, getMessagePropertyFromException(e));
                forward(SIGN_UP);
            }
        } else {
            request.setAttribute(MESSAGE, message);
            forward(SIGN_UP);
        }
    }

    private User checkFieldsAndBuildUser() {
        String name = request.getParameter(NAME);
        String surname = request.getParameter(SURNAME);
        String email = request.getParameter(EMAIL);
        String number = request.getParameter(NUMBER);
        String password = request.getParameter(PASSWORD);
        String passRepeat = request.getParameter(PASSWORD_REPEAT);
        setAttrsForFailCase(name, surname, email, number, password, passRepeat);
        if (!name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !number.isEmpty()
                && !password.isEmpty() && !passRepeat.isEmpty()) {
            if (!password.equals(passRepeat)) {
                message = "passwords";
                return null;
            }
            return new User(null, name, surname, email, number, password);
        }
        message = "fields";
        return null;
    }

    private void setAttrsForFailCase(String name, String surname, String email, String number, String password, String passRepeat) {
        request.setAttribute(NAME, name);
        request.setAttribute(SURNAME, surname);
        request.setAttribute(EMAIL, email);
        request.setAttribute(NUMBER, number);
        request.setAttribute(PASSWORD, password);
        request.setAttribute(PASSWORD_REPEAT, passRepeat);
        request.setAttribute(KEY, request.getParameter(KEY) == null ? EMPTY_STRING : request.getParameter(KEY));
    }

    private String getMessagePropertyFromException(ServiceException e) {

        if (e.getMessage().contains("(" + EMAIL + ")")) {
            return REGISTRATION + DOT + EMAIL;
        } else if (e.getMessage().contains("(" + NUMBER + ")")) {
            return REGISTRATION + DOT + NUMBER;
        } else return REGISTRATION + DOT + "other";
    }
}
