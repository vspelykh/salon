package ua.vspelykh.salon.controller.command.login;

import ua.vspelykh.salon.controller.command.Command;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDate;

import static ua.vspelykh.salon.controller.ControllerConstants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.BIRTHDAY;
import static ua.vspelykh.salon.model.dao.mapper.Column.KEY;
import static ua.vspelykh.salon.util.exception.Messages.*;

/**
 * The RegistrationCommand class extends the abstract Command class and is responsible for processing user
 * registration requests.
 *
 * @version 1.0
 */
public class RegistrationCommand extends Command {

    private String message;

    /**
     * This method processes the user registration request.
     * It builds a User object from the request parameters and saves it to the database via the UserService.
     * If the registration is successful, it sets a success message in the session and redirects the user to the success page.
     * If the registration fails, it sets an appropriate error message in the request and forwards the user back
     * to the registration page.
     *
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void process() throws ServletException, IOException {
        User user = buildUser();
        if (user != null) {
            try {
                saveUser(user, request.getParameter(KEY));
                setSessionAttribute(MESSAGE, MESSAGE_REGISTRATION_SUCCESS);
                redirect(SUCCESS_REDIRECT);
            } catch (ServiceException e) {
                handleServiceException(e);
            }
        } else {
            setRequestAttribute(MESSAGE, message);
            forward(SIGN_UP);
        }
    }

    /**
     * Builds a User object from the request parameters and returns it.
     * If any required fields are empty or if the passwords do not match, it sets an appropriate error message
     * and returns null.
     *
     * @return the built User object, or null if there were errors in the request parameters
     */
    private User buildUser() {
        String name = getParameter(NAME);
        String surname = getParameter(SURNAME);
        String email = getParameter(EMAIL);
        String number = getParameter(NUMBER);
        String birthday = getParameter(BIRTHDAY);
        String password = getParameter(PASSWORD);
        String passRepeat = getParameter(PASSWORD_REPEAT);
        setAttrsForFailCase(name, surname, email, number, birthday, password, passRepeat);
        if (isFieldsEmpty(name, surname, email, number, birthday, password, passRepeat)) {
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
                .birthday(LocalDate.parse(birthday))
                .password(password)
                .build();
    }

    /**
     * This method handles a ServiceException thrown by the UserService during registration.
     * It sets an appropriate error message in the request and forwards the user back to the registration page.
     *
     * @param e the ServiceException thrown by the UserService
     * @throws ServletException if the servlet cannot handle the request for some reason
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    private void handleServiceException(ServiceException e) throws ServletException, IOException {
        if (e.getMessage().contains("(" + EMAIL + ")")) {
            setRequestAttribute(MESSAGE, MESSAGE_REGISTRATION_EMAIL_EXISTS);
        } else if (e.getMessage().contains("(" + NUMBER + ")")) {
            setRequestAttribute(MESSAGE, MESSAGE_REGISTRATION_NUMBER_EXISTS);
        } else {
            setRequestAttribute(MESSAGE, MESSAGE_REGISTRATION_OTHER_ERROR);
        }
        forward(SIGN_UP);
    }

    /**
     * Sets request attributes for fields if registration failed.
     *
     * @param name       the name of the user
     * @param surname    the surname of the user
     * @param email      the email of the user
     * @param number     the phone number of the user
     * @param birthday   the date of birth of the user
     * @param password   the password of the user
     * @param passRepeat the repeated password of the user
     */
    private void setAttrsForFailCase(String name, String surname, String email, String number, String birthday, String password, String passRepeat) {
        setRequestAttribute(NAME, name);
        setRequestAttribute(SURNAME, surname);
        setRequestAttribute(EMAIL, email);
        setRequestAttribute(NUMBER, number);
        setRequestAttribute(BIRTHDAY, birthday);
        setRequestAttribute(PASSWORD, password);
        setRequestAttribute(PASSWORD_REPEAT, passRepeat);
        setRequestAttribute(KEY, request.getParameter(KEY) == null ? EMPTY_STRING : request.getParameter(KEY));
    }

    /**
     * Saves a new user to the database.
     *
     * @param user the user to save or update
     * @param key  key to obtain the master or administrator role, if null - obtain only client role.
     * @throws ServiceException if there is an error in the service layer
     */
    private void saveUser(User user, String key) throws ServiceException {
        if (key != null && !key.isEmpty()) {
            getServiceFactory().getUserService().save(user, key);
        } else {
            getServiceFactory().getUserService().save(user);
        }
    }

    /**
     * Checks whether any of the provided strings are empty or null.
     *
     * @param fields the fields to check
     * @return true if any of the fields are empty or null, false otherwise
     */
    private boolean isFieldsEmpty(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
