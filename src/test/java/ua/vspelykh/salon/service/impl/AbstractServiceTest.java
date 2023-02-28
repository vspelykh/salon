package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.vspelykh.salon.AbstractSalonTest;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.TransactionException;

import static org.mockito.Mockito.verify;

abstract class AbstractServiceTest extends AbstractSalonTest {

    @Mock
    protected Transaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    void verifyTransactionStart() throws TransactionException {
        verify(transaction).start();
    }

    void verifyTransactionCommit() throws TransactionException {
        verify(transaction).commit();
    }

    void verifyTransactionRollback() throws TransactionException {
        verify(transaction).rollback();
    }
}
