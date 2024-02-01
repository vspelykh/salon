package ua.vspelykh.gatewaymicroservice.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    static final String[] PERMIT_ALL = {"/user/login", "/user/refresh", "user/registration",
            "/swagger-ui/**", "/openapi/**", "/webjars/**", "/v3/api-docs/**"
    };

    static final String[] PERMIT_AUTHENTICATED = {"/user/test"};

    static final String[] PERMIT_ADMIN_OR_HAIRDRESSER = {"/admin-hair"};

    static final String[] PERMIT_CLIENT = {"/client"};

    static final String[] PERMIT_ADMIN = {"/user/roles"};

    static final String[] PERMIT_HAIRDRESSER = {"/hair"};
}
