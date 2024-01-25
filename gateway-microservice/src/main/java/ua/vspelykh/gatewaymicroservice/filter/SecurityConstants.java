package ua.vspelykh.gatewaymicroservice.filter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    static final String[] NO_AUTH_URLS = {"/user/login", "/api/v1/login"};

    static final String[] NO_AUTH_PARTS = {"/swagger-ui/**", "/openapi/**", "/webjars/**", "/v3/api-docs/**"};
}