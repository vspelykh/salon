package ua.vspelykh.salon.util.exception;

/**
 * An exception thrown when input validation fails.
 *
 * @version 1.0
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
