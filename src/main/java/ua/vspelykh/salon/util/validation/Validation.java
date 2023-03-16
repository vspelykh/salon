package ua.vspelykh.salon.util.validation;

import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ValidationException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * A utility class containing methods for validating user input.
 *
 * @version 1.0
 */
public class Validation {

    private Validation() {
    }

    /**
     * Checks if the provided email is in a valid format.
     *
     * @param email the email address to check
     * @throws ValidationException if the email is not in a valid format
     */
    public static void checkEmail(String email) throws ValidationException {
        if (!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            throw new ValidationException(EMAIL);
        }
    }

    /**
     * Checks if the provided phone number is in a valid format.
     *
     * @param number the phone number to check
     * @throws ValidationException if the phone number is not in a valid format
     */
    public static void checkNumber(String number) throws ValidationException {
        if (!number.matches("^\\+[0-9]{12}")) {
            throw new ValidationException(NUMBER);
        }
    }

    /**
     * Checks if the provided name and surname are in a valid format.
     *
     * @param name    the name to check
     * @param surname the surname to check
     * @throws ValidationException if the name or surname is not in a valid format
     */
    public static void checkNameAndSurname(String name, String surname) throws ValidationException {
        String regex = "[a-zA-Z]{2,25}|[а-яА-Я]{2,25}";
        if (!name.matches(regex) || !surname.matches(regex)) {
            throw new ValidationException(NAME);
        }
    }

    /**
     * Checks if the provided password is in a valid format.
     *
     * @param password the password to check
     * @throws ValidationException if the password is not in a valid format
     */
    public static void checkPassword(String password) throws ValidationException {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,20}$")) {
            throw new ValidationException(PASSWORD);
        }
    }

    /**
     * Checks if the provided user object contains valid input values.
     *
     * @param user the user object to check
     * @throws ValidationException if any of the input values are not valid
     */
    public static void checkUser(User user) throws ValidationException {
        checkEmail(user.getEmail());
        checkNumber(user.getNumber());
        checkNameAndSurname(user.getName(), user.getSurname());
    }
}
