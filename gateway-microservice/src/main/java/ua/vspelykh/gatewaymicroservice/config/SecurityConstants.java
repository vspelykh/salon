package ua.vspelykh.gatewaymicroservice.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    static final String[] PERMIT_ALL = {
            /*USER MS*/
            "/user/login", "/user/refresh", "user/registration", "/user/token",

            /*SWAGGER*/
            "/swagger-ui/**", "/openapi/**", "/webjars/**", "/v3/api-docs/**",

            /*FRONT*/
            "/ui/home", "/ui/login", "/ui/sign-up", "/ui/images/**", "/ui/styles/**", "/ui/js/**",
            "/ui/forbidden", "/ui/masters"
    };

    static final String[] PERMIT_AUTHENTICATED = {
            /*USER MS*/
            "/user/test",

            /*FRONT*/
            "/ui/profile"
    };

    static final String[] PERMIT_ADMIN_OR_HAIRDRESSER = {"/admin-hair"};

    static final String[] PERMIT_CLIENT = {"/client"};

    static final String[] PERMIT_ADMIN = {"/user/roles"};

    static final String[] PERMIT_HAIRDRESSER = {"/hair"};
}
