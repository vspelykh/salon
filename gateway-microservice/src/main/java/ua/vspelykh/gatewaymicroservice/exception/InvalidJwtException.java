package ua.vspelykh.gatewaymicroservice.exception;

public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(String message) {
        super(message);
    }
}

