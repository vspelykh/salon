package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.model.dao.WorkingDayDao;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestDates;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestWorkingDay;
import static ua.vspelykh.salon.model.dao.impl.SqlConstants.WorkingDay.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.SalonUtils.getDate;

class WorkingDayDaoImplTest extends AbstractDaoTest {

    private WorkingDayDao mockWorkingDayDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockWorkingDayDao = new WorkingDayDaoImpl();
        mockWorkingDayDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            int id = mockWorkingDayDao.create(getTestWorkingDay());

            verifyQueryWithGeneratedKey(INSERT_WORKING_DAY);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setDate(++k, Date.valueOf(DATE_VALUE.toLocalDate()));
            verify(statement).setTime(++k, Time.valueOf(START_TIME_VALUE));
            verify(statement).setTime(++k, Time.valueOf(END_TIME_VALUE));
            verify(statement).getGeneratedKeys();
            assertEquals(ID_VALUE, id);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void createWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent();
            assertThrows(DaoException.class, () -> mockWorkingDayDao.create(getTestWorkingDay()));

            verifyQueryWithGeneratedKey(INSERT_WORKING_DAY);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() {
        WorkingDay testWorkingDay = getTestWorkingDay();
        assertThrows(UnsupportedOperationException.class, () -> mockWorkingDayDao.update(testWorkingDay));
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void save() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockWorkingDayDao.save(ID_VALUE, getTestDates(), Time.valueOf(START_TIME_VALUE), Time.valueOf(END_TIME_VALUE));

            verifyQuery(SAVE_WORKING_DAYS);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            for (String date : getTestDates()) {
                verify(statement).setInt(++k, ID_VALUE);
                verify(statement).setDate(++k, getDate(date));
                verify(statement).setTime(++k, Time.valueOf(START_TIME_VALUE));
                verify(statement).setTime(++k, Time.valueOf(END_TIME_VALUE));
            }
            verify(statement).setTime(++k, Time.valueOf(START_TIME_VALUE));
            verify(statement).setTime(++k, Time.valueOf(END_TIME_VALUE));
        }
    }

    @Test
    void getDayByUserIdAndDate() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            WorkingDay workingDay = mockWorkingDayDao.getDayByUserIdAndDate(ID_VALUE, DATE_VALUE.toLocalDate());
            assertEquals(getTestWorkingDay(), workingDay);
            verifyQuery(SELECT_WORKING_DAY);
        }
    }

    @Test
    void getWorkingDaysByUserId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<WorkingDay> workingDays = mockWorkingDayDao.getWorkingDaysByUserId(ID_VALUE);
            assertEquals(1, workingDays.size());
            assertEquals(getTestWorkingDay(), workingDays.get(0));

            verifyQuery(SELECT_WORKING_DAYS_BY_ID);
        }
    }

    @Test
    void deleteWorkingDaysByUserIdAndDatesArray() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            assertDoesNotThrow(() -> mockWorkingDayDao.deleteWorkingDaysByUserIdAndDatesArray(ID_VALUE, getTestDates()));

            verifyQuery(DELETE_WORKING_DAYS);
        }
    }

    private PreparedStatement mockPrepareStatement() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        doNothing().when(preparedStatement).setDate(isA(int.class), isA(Date.class));
        doNothing().when(preparedStatement).setTime(isA(int.class), isA(Time.class));
        when(preparedStatement.execute()).thenReturn(true);
        when(mockResultSet.next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(preparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getInt(1)).thenReturn(ID_VALUE);
        return preparedStatement;
    }

    private void mockResultSetIfPresent() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getInt(USER_ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getDate(DATE)).thenReturn(Date.valueOf(DATE_VALUE.toLocalDate()));
        when(mockResultSet.getTime(TIME_START)).thenReturn(Time.valueOf(START_TIME_VALUE));
        when(mockResultSet.getTime(TIME_END)).thenReturn(Time.valueOf(END_TIME_VALUE));
    }
}