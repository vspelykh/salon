package ua.vspelykh.salon.util.validation;

import ua.vspelykh.salon.model.User;

public class Validation {

    private Validation() {
    }

    public static void checkEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            throw new IllegalArgumentException("email is not valid");
        }
    }

    public static void checkNumber(String number) {
        if (!number.matches("^\\+[0-9]{12}")) {
            throw new IllegalArgumentException("mobile number is not valid");
        }
    }

    public static void checkNameAndSurname(String name, String surname) {
        String regex = "[a-zA-Z]{2,25}|[а-яА-Я]{2,25}";
        if (!name.matches(regex) || !surname.matches(regex)) {
            throw new IllegalArgumentException("name and/or surname is not valid");
        }
    }

    public static void checkPassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-zа-я])(?=.*[A-ZА-Я]).{8,20}$")) {
            throw new IllegalArgumentException("password is not valid");
        }
    }

    public static void checkUser(User user) {
        checkEmail(user.getEmail());
        checkNumber(user.getNumber());
        checkNameAndSurname(user.getName(), user.getSurname());
    }
}
