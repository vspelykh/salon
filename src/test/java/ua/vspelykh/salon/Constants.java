package ua.vspelykh.salon;

import ua.vspelykh.salon.model.entity.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public interface Constants {

    int ID_VALUE = 1;
    String NAME_VALUE = "James";
    String SURNAME_VALUE = "Hetfield";
    String EMAIL_VALUE = "papahet@gmail.com";
    String NUMBER_VALUE = "+380976543211";
    String PASSWORD_VALUE = "Password1";
    Set<Role> ROLES_VALUE = Set.of(Role.CLIENT);

    int ERROR_CODE = -1;

    int CLIENT_ID_VALUE = 2;
    int CONTINUANCE_VALUE = 30;
    LocalDateTime DATE_VALUE = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0));
    int PRICE_VALUE = 200;

}
