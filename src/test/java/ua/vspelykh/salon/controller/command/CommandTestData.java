package ua.vspelykh.salon.controller.command;

import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;

import java.util.Set;

import static ua.vspelykh.salon.Constants.*;

public class CommandTestData {

    public static int ID_VALUE_2 = 2;

    public static User getTestMaster() {
        return User.builder().id(ID_VALUE_2)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .password(PASSWORD_VALUE)
                .roles(Set.of(Role.HAIRDRESSER))
                .build();
    }
}
