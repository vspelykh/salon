package ua.vspelykh.gatewaymicroservice.exception;

public class FeignLoginException extends RuntimeException {

    public FeignLoginException(String message) {
        super(message);
    }
}
