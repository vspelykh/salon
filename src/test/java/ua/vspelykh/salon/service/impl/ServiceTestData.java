package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.dto.UserDto;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;

import java.util.List;
import java.util.Set;

import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestAppointmentItem;

public class ServiceTestData {

    public static User getTestMaster() {
        return User.builder().id(ID_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .password(PASSWORD_VALUE)
                .roles(Set.of(Role.HAIRDRESSER))
                .build();
    }

    public static AppointmentDto getTestAppointmentDto() {
        return AppointmentDto.builder().id(ID_VALUE)
                .master(getTestMasterDto())
                .client(getTestClientDto())
                .continuance(CONTINUANCE_VALUE)
                .date(DATE_VALUE)
                .price(PRICE_VALUE)
                .discount(1)
                .appointmentItems(List.of(getTestAppointmentItem()))
                .status(AppointmentStatus.SUCCESS)
                .paymentStatus(PaymentStatus.PAID_BY_CARD)
                .build();
    }

    public static UserDto getTestMasterDto() {
        return UserDto.builder().id(ID_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .roles(ROLES_VALUE)
                .build();
    }

    public static UserDto getTestClientDto() {
        return UserDto.builder().id(2)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .roles(ROLES_VALUE)
                .build();
    }
}
