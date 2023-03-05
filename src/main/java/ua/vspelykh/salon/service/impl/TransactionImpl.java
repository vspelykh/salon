package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class implements the Transaction interface and provides methods for managing transactions with
 * a database connection.
 *
 * @version 1.0
 */
public class TransactionImpl implements Transaction {

    private Connection connection;

    private static final Logger LOG = LogManager.getLogger(TransactionImpl.class);

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Start a new transaction by setting the database connection to auto-commit false.
     *
     * @throws TransactionException if there is an issue starting the transaction
     */
    @Override
    public void start() throws TransactionException {
        try {
            LOG.info("New transaction started");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOG.error(String.format("Fail to start transaction. Issue: %s", e.getMessage()));
            throw new TransactionException(e);
        }
    }

    /**
     * Commit the current transaction by committing the database connection and setting auto-commit to true.
     *
     * @throws TransactionException if there is an issue committing the transaction
     */
    @Override
    public void commit() throws TransactionException {
        try {
            connection.commit();
            connection.setAutoCommit(true);
            LOG.info("Transaction committed.");
        } catch (SQLException e) {
            LOG.error(String.format("Fail to commit transaction. Issue: %s", e.getMessage()));
            throw new TransactionException(e);
        }
    }

    /**
     * Rollback the current transaction by rolling back the database connection and setting auto-commit to true.
     *
     * @throws TransactionException if there is an issue rolling back the transaction
     */
    @Override
    public void rollback() throws TransactionException {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            LOG.info("Rollback of the transaction");
        } catch (SQLException e) {
            LOG.error(String.format("Fail to rollback transaction. Issue: %s", e.getMessage()));
            throw new TransactionException(e);
        }
    }
}
