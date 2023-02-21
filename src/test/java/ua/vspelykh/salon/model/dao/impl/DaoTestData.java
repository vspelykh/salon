package ua.vspelykh.salon.model.dao.impl;

import ua.vspelykh.salon.model.entity.*;

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

    public static UserLevel getTestUserLevel() {
        return UserLevel.builder().masterId(ID_VALUE)
                .level(MastersLevel.YOUNG)
                .about(ABOUT_VALUE)
                .aboutUa(ABOUT_UA_VALUE)
                .isActive(true)
                .build();
    }

    public static AppointmentItem getTestAppointmentItem() {
        return AppointmentItem.builder().id(ID_VALUE)
                .appointmentId(ID_VALUE)
                .serviceId(ID_VALUE)
                .build();
    }

    public static BaseService getTestBaseService() {
        return BaseService.builder().id(ID_VALUE)
                .categoryId(ID_VALUE)
                .service(SERVICE_VALUE)
                .serviceUa(SERVICE_UA_VALUE)
                .price(PRICE_VALUE)
                .build();
    }
}
