package ua.vspelykh.salon.util.exception;

/**
 * Exception that indicates an error occurred while performing a transaction with the database.
 *
 * @version 1.0
 */
public class TransactionException extends Exception {

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
