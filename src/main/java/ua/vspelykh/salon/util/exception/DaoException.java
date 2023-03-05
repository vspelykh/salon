package ua.vspelykh.salon.util.exception;

/**
 * Custom exception class for DAO layer errors.
 *
 * @version 1.0
 */
public class DaoException extends Exception {

    public DaoException() {
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}