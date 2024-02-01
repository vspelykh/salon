package ua.vspelykh.usermicroservice.controller.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwaggerConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Code {
        public static final String CODE_200 = "200";
        public static final String CODE_202 = "202";
        public static final String CODE_204 = "204";
        public static final String CODE_400 = "400";
        public static final String CODE_401 = "401";
        public static final String CODE_403 = "403";
        public static final String CODE_404 = "404";
        public static final String CODE_201 = "201";

        public static final String CODE_409 = "409";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Message {
        public static final String MESSAGE_200 = "OK";
        public static final String MESSAGE_202 = "Accepted";
        public static final String MESSAGE_400 = "Received incorrect data";
        public static final String MESSAGE_401 = "You are not authorized to view the resource";
        public static final String MESSAGE_403 = "Accessing the resource you were trying to reach is forbidden";
        public static final String MESSAGE_404 = "The resource you were trying to reach is not found";
        public static final String MESSAGE_201 = "The resource was successfully created";
        public static final String MESSAGE_409 = "Account with such email/number already exist";
    }
}
