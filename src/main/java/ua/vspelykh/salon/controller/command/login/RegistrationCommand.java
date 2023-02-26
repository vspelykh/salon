package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.KEY;
import static ua.vspelykh.salon.util.exception.Messages.*;

public class RegistrationCommand extends Command {

    private String message;

    @Override
    public void process() throws ServletException, IOException {
        User user = buildUser();
        if (user != null) {
            try {
                saveUser(user, request.getParameter(KEY));
                request.getSession().setAttribute(MESSAGE, MESSAGE_REGISTRATION_SUCCESS);
                redirect(context.getContextPath() + SUCCESS_REDIRECT);
            } catch (ServiceException e) {
                handleServiceException(e);
            }
        } else {
            request.setAttribute(MESSAGE, message);
            forward(SIGN_UP);
        }
    }

    private User buildUser() {
        String name = request.getParameter(NAME);
        String surname = request.getParameter(SURNAME);
        String email = request.getParameter(EMAIL);
        String number = request.getParameter(NUMBER);
        String password = request.getParameter(PASSWORD);
        String passRepeat = request.getParameter(PASSWORD_REPEAT);
        setAttrsForFailCase(name, surname, email, number, password, passRepeat);
        if (isFieldsEmpty(name, surname, email, number, password, passRepeat)) {
            message = MESSAGE_FIELDS_EMPTY;
            return null;
        }

        if (!password.equals(passRepeat)) {
            message = MESSAGE_PASSWORDS_MISMATCH;
            return null;
        }
        return User.builder().name(name)
                .surname(surname)
                .email(email)
                .number(number)
                .password(password)
                .build();
    }

    private void handleServiceException(ServiceException e) throws ServletException, IOException {
        if (e.getMessage().contains("(" + EMAIL + ")")) {
            request.setAttribute(MESSAGE, MESSAGE_REGISTRATION_EMAIL_EXISTS);
        } else if (e.getMessage().contains("(" + NUMBER + ")")) {
            request.setAttribute(MESSAGE, MESSAGE_REGISTRATION_NUMBER_EXISTS);
        } else {
            request.setAttribute(MESSAGE, MESSAGE_REGISTRATION_OTHER_ERROR);
        }
        forward(SIGN_UP);
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

    private void saveUser(User user, String key) throws ServiceException {
        if (key != null && !key.isEmpty()) {
            getServiceFactory().getUserService().save(user, key);
        } else {
            getServiceFactory().getUserService().save(user);
        }
    }

    private boolean isFieldsEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
