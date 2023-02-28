package ua.vspelykh.salon.util.validation;

import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.exception.ServiceException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

public class Validation {

    private Validation() {
    }

    public static void checkEmail(String email) throws ServiceException {
        if (!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            throw new ServiceException(EMAIL);
        }
    }

    public static void checkNumber(String number) throws ServiceException {
        if (!number.matches("^\\+[0-9]{12}")) {
            throw new ServiceException(NUMBER);
        }
    }

    public static void checkNameAndSurname(String name, String surname) throws ServiceException {
        String regex = "[a-zA-Z]{2,25}|[а-яА-Я]{2,25}";
        if (!name.matches(regex) || !surname.matches(regex)) {
            throw new ServiceException(NAME);
        }
    }

    public static void checkPassword(String password) throws ServiceException {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,20}$")) {
            throw new ServiceException(PASSWORD);
        }
    }

    public static void checkUser(User user) throws ServiceException {
        checkEmail(user.getEmail());
        checkNumber(user.getNumber());
        checkNameAndSurname(user.getName(), user.getSurname());
    }
}
