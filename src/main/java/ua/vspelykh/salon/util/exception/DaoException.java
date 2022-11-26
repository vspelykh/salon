package ua.vspelykh.salon.util.exception;

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