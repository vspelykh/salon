package ua.vspelykh.salon.util.exception;

/**
 * Exception thrown when a service layer operation fails.
 *
 * @version 1.0
 */
public class ServiceException extends Exception {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String message) {
        super((message));
    }
}
