package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.service.ServiceFactory;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

import static ua.vspelykh.salon.controller.ControllerConstants.*;

public class RegistrationCommand extends Command {

    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String EMAIL = "email";
    private static final String NUMBER = "number";
    private static final String PASSWORD = "password";
    private static final String PASSWORD_REPEAT = "passwordRepeat";

    private UserService userService = ServiceFactory.getUserService();

    @Override
    public void process() throws ServletException, IOException {

        User user = checkFieldsAndBuildUser();
        if (user != null) {
            try {
                userService.save(user);
                redirect(context.getContextPath() + SUCCESS_REDIRECT);
            } catch (ServiceException e) {
                request.setAttribute(MESSAGE, "Failed");
                forward(SIGN_UP);
            }
        } else {
            request.setAttribute(MESSAGE, "Not all fields are fulled");
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
        if (name.isPresent() && surname.isPresent() && email.isPresent() && number.isPresent()
                && password.isPresent() && passRepeat.isPresent() && password.get().equals(passRepeat.get())) {
            return new User(null, name.get(), surname.get(), email.get(), number.get(), password.get());
        }
        return null;
    }
}
