package ua.vspelykh.salon.dao.impl;

import ua.vspelykh.salon.model.entity.User;

import static ua.vspelykh.salon.Constants.*;

public class DaoTestData {

    public static User getTestUser(){
        return User.builder().id(ID_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .password(PASSWORD_VALUE)
                .roles(ROLES_VALUE)
                .build();

    }
}
