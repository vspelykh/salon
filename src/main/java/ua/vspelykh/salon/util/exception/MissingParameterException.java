package ua.vspelykh.salon.util.exception;

public class MissingParameterException extends IllegalArgumentException {

    public MissingParameterException(String message) {
        super(message);
    }

    public MissingParameterException(String paramName, String message) {
        super("Parameter " + paramName + " is required and cannot be null or empty. " + message);
    }

}
