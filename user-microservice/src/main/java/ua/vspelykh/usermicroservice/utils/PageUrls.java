package ua.vspelykh.usermicroservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUrls {

    public static final String HOME_URL = "/";
    public static final String API = "/api";

    public static final String LOGIN_URL = "api/v1/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String WITHDRAW_URL = "/withdraw";
    public static final String AMOUNT_URL = "/amount";
    public static final String STRATEGIES_URL = "/strategies";
    public static final String PROCESS_URL = "/process";
    public static final String RESULT_URL = "/result";

    public static final String TRANSFER_URL = "/transfer";
    public static final String ACCOUNTS_URL = "/accounts";
    public static final String SEND_URL = "/send";

    public static final String BALANCE_URL = "/balance";

    public static final String ALL_API = "/api/**";
    public static final String ALL_WITHDRAW_URLS = "/withdraw/**";
    public static final String ALL_TRANSFER_PAGES = "/transfer/**";
}