package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionImpl implements Transaction {

    private Connection connection;

    private static final Logger LOG = LogManager.getLogger(TransactionImpl.class);

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

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
