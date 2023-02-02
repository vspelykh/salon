package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
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
                if (request.getParameter(KEY) != null) {
                    getServiceFactory().getUserService().save(user, request.getParameter(KEY));
                } else {
                    getServiceFactory().getUserService().save(user);
                }
                redirect(context.getContextPath() + SUCCESS_REDIRECT);
            } catch (ServiceException e) {
                request.setAttribute(MESSAGE, e.getMessage());
                forward(SIGN_UP);
            }
        } else {
            request.setAttribute(MESSAGE, message);
            forward(SIGN_UP);
        }
    }

    private User checkFieldsAndBuildUser() {
        Optional<String> name = Optional.ofNullable(request.getParameter(NAME));
        Optional<String> surname = Optional.ofNullable(request.getParameter(SURNAME));
        Optional<String> email = Optional.ofNullable(request.getParameter(EMAIL));
        Optional<String> number = Optional.ofNullable(request.getParameter(NUMBER));
        Optional<String> password = Optional.ofNullable(request.getParameter(PASSWORD));
        Optional<String> passRepeat = Optional.ofNullable(request.getParameter(PASSWORD_REPEAT));
        setAttrsForFailCase(name, surname, email, number, password, passRepeat);
        if (name.isPresent() && surname.isPresent() && email.isPresent() && number.isPresent()
                && password.isPresent() && passRepeat.isPresent()) {
            if (!password.get().equals(passRepeat.get())) {
                message = "passwords";
                return null;
            }
            return new User(null, name.get(), surname.get(), email.get(), number.get(), password.get());
        }
        message = "fields";
        return null;
    }

    private void setAttrsForFailCase(Optional<String> name, Optional<String> surname, Optional<String> email, Optional<String> number, Optional<String> password, Optional<String> passRepeat) {
        request.setAttribute(NAME, name.orElse(EMPTY_STRING));
        request.setAttribute(SURNAME, surname.orElse(EMPTY_STRING));
        request.setAttribute(EMAIL, email.orElse(EMPTY_STRING));
        request.setAttribute(NUMBER, number.orElse(EMPTY_STRING));
        request.setAttribute(PASSWORD, password.orElse(EMPTY_STRING));
        request.setAttribute(PASSWORD_REPEAT, passRepeat.orElse(EMPTY_STRING));
        request.setAttribute(KEY, request.getParameter(KEY) == null ? EMPTY_STRING : request.getParameter(KEY));
    }
}
