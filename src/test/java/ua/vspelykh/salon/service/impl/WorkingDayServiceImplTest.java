package ua.vspelykh.salon.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ua.vspelykh.salon.model.dao.WorkingDayDao;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.sql.Time;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestWorkingDay;

class WorkingDayServiceImplTest extends AbstractServiceTest {

    @Mock
    private WorkingDayDao workingDayDao;

    @InjectMocks
    private WorkingDayService workingDayService = new WorkingDayServiceImpl();

    private final String[] date = {DATE_VALUE.toLocalDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN))};


    @Test
    void save() throws ServiceException, TransactionException, DaoException {
        WorkingDay testWorkingDay = getTestWorkingDay();
        testWorkingDay.setId(null);
        doNothing().when(transaction).start();
        doReturn(ID_VALUE).when(workingDayDao).create(testWorkingDay);
        doNothing().when(transaction).commit();
        workingDayService.save(testWorkingDay);
        verifyTransactionStart();
        verify(workingDayDao, times(1)).create(testWorkingDay);
        verifyTransactionCommit();
    }

    @Test
    void saveDaysArray() throws DaoException, ServiceException, TransactionException {
        doNothing().when(workingDayDao).save(ID_VALUE, date, Time.valueOf(START_TIME_VALUE), Time.valueOf(END_TIME_VALUE));

        workingDayService.save(ID_VALUE, date, Time.valueOf(START_TIME_VALUE), Time.valueOf(END_TIME_VALUE));
        verifyTransactionStart();
        verifyTransactionCommit();

    }

    @Test
    void delete() throws ServiceException, TransactionException, DaoException {
        workingDayService.delete(ID_VALUE);
        verify(transaction).start();
        verify(workingDayDao).removeById(ID_VALUE);
        verify(transaction).commit();
    }

    @Test
    void deleteDaysArray() throws DaoException, ServiceException, TransactionException {
        doNothing().when(workingDayDao).deleteByUserIdAndDatesArray(ID_VALUE, date);

        workingDayService.deleteByUserIdAndDatesArray(ID_VALUE, date);
        verifyTransactionStart();
        verifyTransactionCommit();
    }

    @Test
    void deleteThrowsException() throws TransactionException, DaoException {
        doThrow(new DaoException()).when(workingDayDao).removeById(ID_VALUE);
        assertThrows(ServiceException.class, () -> workingDayService.delete(ID_VALUE));
        verify(transaction).start();
        verify(workingDayDao).removeById(ID_VALUE);
        verifyTransactionRollback();
    }

}