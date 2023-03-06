package ua.vspelykh.salon.model.dao.postgres;

import ua.vspelykh.salon.model.entity.*;
import ua.vspelykh.salon.util.AppointmentFilter;
import ua.vspelykh.salon.util.MasterFilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ua.vspelykh.salon.Constants.*;

public class DaoTestData {

    public static User getTestUser() {
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

    public static Feedback getTestFeedback() {
        return Feedback.builder().id(ID_VALUE)
                .appointmentId(ID_VALUE)
                .mark(MARK_VALUE)
                .comment(COMMENT_VALUE)
                .date(DATE_VALUE)
                .build();
    }

    public static MasterService getTestMasterService() {
        return MasterService.builder().id(ID_VALUE)
                .masterId(ID_VALUE)
                .baseServiceId(ID_VALUE)
                .continuance(CONTINUANCE_VALUE)
                .build();
    }

    public static WorkingDay getTestWorkingDay() {
        return WorkingDay.builder().id(ID_VALUE)
                .userId(ID_VALUE)
                .date(DATE_VALUE.toLocalDate())
                .timeStart(START_TIME_VALUE)
                .timeEnd(END_TIME_VALUE)
                .build();
    }

    public static String[] getTestDates() {
        return new String[]{(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern(DATE_PATTERN))),
                (LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern(DATE_PATTERN)))};
    }

    public static AppointmentFilter getAppointmentFilter() {
        return AppointmentFilter.builder().masterId(ID_VALUE)
                .dateFrom(DATE_VALUE.toLocalDate())
                .dateTo(DATE_VALUE.toLocalDate())
                .status(AppointmentStatus.SUCCESS)
                .paymentStatus(PaymentStatus.PAID_BY_CARD)
                .build();
    }

    public static MasterFilter createMasterFilter(List<MastersLevel> levels, List<Integer> serviceIds,
                                                  List<Integer> categoriesIds, String search) {
        return MasterFilter.builder()
                .levels(levels)
                .serviceIds(serviceIds)
                .categoriesIds(categoriesIds)
                .search(search)
                .build();
    }
}
