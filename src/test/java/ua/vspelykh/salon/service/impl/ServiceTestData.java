package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.model.dto.*;
import ua.vspelykh.salon.model.entity.*;

import java.util.List;
import java.util.Set;

import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.UA_LOCALE;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.*;

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

    public static ServiceCategory getTestCategory() {
        return ServiceCategory.builder()
                .id(ID_VALUE)
                .name(CATEGORY_VALUE)
                .nameUa(CATEGORY_UA_VALUE)
                .build();
    }

    public static BaseServiceDto getTestBaseServiceDto() {
        return new BaseServiceDto.BaseServiceDtoBuilder(getTestBaseService(), getTestCategory(), UA_LOCALE).build();
    }

    public static FeedbackDto getTestFeedbackDto() {
        return new FeedbackDto.FeedbackDtoBuilder(getTestUser(), getTestFeedback()).build();
    }

    public static MasterServiceDto getTestMasterServiceDto() {
        return MasterServiceDto.builder().id(ID_VALUE)
                .masterId(ID_VALUE)
                .service(getTestBaseServiceDto())
                .continuance(CONTINUANCE_VALUE)
                .build();
    }

    public static Invitation getTestInvitation() {
        return Invitation.builder().id(ID_VALUE)
                .email(EMAIL_VALUE)
                .date(DATE_VALUE.toLocalDate())
                .role(Role.ADMINISTRATOR)
                .key(KEY_VALUE)
                .build();
    }

    public static UserMasterDTO getTestUserMasterDto() {
        return UserMasterDTO.build(getTestMaster(), getTestUserLevel(), MARK_VALUE, UA_LOCALE);
    }

    public static User getTestUserWithRole() {
        return User.builder().id(ID_VALUE)
                .name(NAME_VALUE)
                .surname(SURNAME_VALUE)
                .email(EMAIL_VALUE)
                .number(NUMBER_VALUE)
                .birthday(BIRTHDAY_VALUE)
                .password(PASSWORD_VALUE)
                .roles(ROLES_VALUE)
                .build();
    }

}
