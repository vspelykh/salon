package ua.vspelykh.usermicroservice.controller.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConstants {
    public static final String NUMBER_REGEX = "^\\+[0-9]{12}";
    public static final String USER_NAME_REGEX = "^[A-Z][a-zA-Z]{1,18}$";
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PASS_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&!])[A-Za-z\\d@#$%^&!]{8,20}$";
    public static final String PASS_NO_SPACES_REGEX = "^\\S+$";
}