package ua.vspelykh.usermicroservice.controller.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegexConstants {
    public static final String NUMBER_REGEX = "^\\+1(647|778|438|343)\\d{7}$";
    public static final String USER_NAME_REGEX = "^[A-Z][a-zA-Z]{1,18}$";
    public static final String EMAIL_REGEX = "^[A-Za-z][A-Za-z0-9._-]*@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\\.[A-Za-z]{2,})+$";
    public static final String PASS_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&!])[A-Za-z\\d@#$%^&!]{8,20}$";
    public static final String PASS_NO_SPACES_REGEX = "^\\S+$";
}