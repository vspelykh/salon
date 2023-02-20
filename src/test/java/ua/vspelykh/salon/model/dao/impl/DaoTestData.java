package ua.vspelykh.salon.model.dao.impl;

import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.model.entity.User;

import static ua.vspelykh.salon.Constants.*;

public class DaoTestData {

    public static User getTestUser() {
        return User.builder().id(ID_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .password(PASSWORD_VALUE)
                .roles(ROLES_VALUE)
                .build();
    }

    public static Appointment getTestAppointment() {
        return Appointment.builder().id(ID_VALUE)
                .masterId(ID_VALUE)
                .clientId(CLIENT_ID_VALUE)
                .continuance(CONTINUANCE_VALUE)
                .date(DATE_VALUE)
                .price(PRICE_VALUE)
                .discount(ID_VALUE)
                .status(AppointmentStatus.SUCCESS)
                .paymentStatus(PaymentStatus.PAID_BY_CARD)
                .build();
    }
}
