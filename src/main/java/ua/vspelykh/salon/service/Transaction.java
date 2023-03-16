package ua.vspelykh.salon.service;

import ua.vspelykh.salon.util.exception.TransactionException;

/**
 * The Transaction interface provides methods for managing database transactions.
 *
 * @version 1.0
 */
public interface Transaction {

    /**
     * Starts a new database transaction.
     *
     * @throws TransactionException if an error occurs while starting the transaction
     */
    void start() throws TransactionException;

    /**
     * Commits the current database transaction.
     *
     * @throws TransactionException if an error occurs while committing the transaction
     */
    void commit() throws TransactionException;

    /**
     * Rolls back the current database transaction.
     *
     * @throws TransactionException if an error occurs while rolling back the transaction
     */
    void rollback() throws TransactionException;
}