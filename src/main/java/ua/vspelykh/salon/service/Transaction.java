package ua.vspelykh.salon.service;

import ua.vspelykh.salon.util.exception.TransactionException;

public interface Transaction {

    void start() throws TransactionException;

    void commit() throws TransactionException;

    void rollback() throws TransactionException;
}