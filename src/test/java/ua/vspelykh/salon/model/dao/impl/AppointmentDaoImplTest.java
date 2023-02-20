package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestAppointment;
import static ua.vspelykh.salon.model.dao.impl.SqlConstants.Appointment.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

class AppointmentDaoImplTest extends AbstractDaoTest {

    private AppointmentDao mockAppointmentDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockAppointmentDao = new AppointmentDaoImpl();
        mockAppointmentDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            Appointment testAppointment = getTestAppointment();
            testAppointment.setId(null);
            int id = mockAppointmentDao.create(testAppointment);

            verifySqlWithGeneratedKey(INSERT_APPOINTMENT);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setInt(++k, CLIENT_ID_VALUE);
            verify(statement).setInt(++k, CONTINUANCE_VALUE);
            verify(statement).setTimestamp(++k, Timestamp.valueOf(DATE_VALUE));
            verify(statement).setInt(++k, PRICE_VALUE);
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setString(++k, AppointmentStatus.SUCCESS.name());
            verify(statement).setString(++k, PaymentStatus.PAID_BY_CARD.name());
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
            assertThrows(DaoException.class, () -> mockAppointmentDao.create(getTestAppointment()));

            verifySqlWithGeneratedKey(INSERT_APPOINTMENT);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockAppointmentDao.update(getTestAppointment()));

            verifySql(UPDATE_APPOINTMENT);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockAppointmentDao.update(getTestAppointment()));

            verifySql(UPDATE_APPOINTMENT);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void getByDateAndMasterId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<Appointment> appointments = mockAppointmentDao.getByDateAndMasterId(LocalDate.now(), ID_VALUE);

            assertEquals(1, appointments.size());
            assertEquals(getTestAppointment(), appointments.get(0));

            verifySql(SELECT_BY_DATE_AND_MASTER_ID);
        }
    }

    @Test
    void getAllByDate() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<Appointment> appointments = mockAppointmentDao.getAllByDate(LocalDate.now());

            assertEquals(1, appointments.size());
            assertEquals(getTestAppointment(), appointments.get(0));
        }
    }

    @Test
    void getAllByDateNotFound() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfAbsent();

            assertEquals(Collections.emptyList(), mockAppointmentDao.getAllByDate(LocalDate.now()));
        }
    }

    @Test
    void getFiltered() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<Appointment> appointments = mockAppointmentDao.getFiltered(ID_VALUE, DATE_VALUE.toLocalDate(),
                    DATE_VALUE.toLocalDate(), AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD, 5, 1);

            assertEquals(1, appointments.size());
            assertEquals(getTestAppointment(), appointments.get(0));
        }
    }

    @Test
    void getCountOfAppointments() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            int count = mockAppointmentDao.getCountOfAppointments(ID_VALUE, DATE_VALUE.toLocalDate(),
                    DATE_VALUE.toLocalDate(), AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD);

            assertEquals(1, count);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlTest")
    void getFilteredCheckSql(LocalDate from, LocalDate to, AppointmentStatus status, PaymentStatus paymentStatus,
                             String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            mockAppointmentDao.getFiltered(ID_VALUE, from, to, status, paymentStatus, 1, 5);
            verifySql(sqlExpected);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlCountTest")
    void getCountOfAppointmentsCheckSql(LocalDate from, LocalDate to, AppointmentStatus status, PaymentStatus paymentStatus,
                                        String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            mockAppointmentDao.getCountOfAppointments(ID_VALUE, from, to, status, paymentStatus);
            verifySql(sqlExpected);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> dataForSqlTest() {
        LocalDate date = LocalDate.from(DATE_VALUE);
        AppointmentStatus status = AppointmentStatus.SUCCESS;
        PaymentStatus paymentStatus = PaymentStatus.PAID_BY_CARD;

        return Arrays.asList(new Object[][]{
                {null, null, null, null, GET_APPOINTMENTS_ALL},
                {date, date, status, paymentStatus, GET_APPOINTMENTS_FILTERED_1},
                {null, date, null, paymentStatus, GET_APPOINTMENTS_FILTERED_2},
                {date, null, status, null, GET_APPOINTMENTS_FILTERED_3},
        });
    }

    @Parameterized.Parameters
    public static Collection<Object[]> dataForSqlCountTest() {
        LocalDate date = LocalDate.from(DATE_VALUE);
        AppointmentStatus status = AppointmentStatus.SUCCESS;
        PaymentStatus paymentStatus = PaymentStatus.PAID_BY_CARD;

        return Arrays.asList(new Object[][]{
                {null, null, null, null, SELECT_COUNT_1},
                {date, date, status, paymentStatus, SELECT_COUNT_2},
                {null, date, null, paymentStatus, SELECT_COUNT_3},
                {date, null, status, null, SELECT_COUNT_4},
        });
    }

    private PreparedStatement mockPrepareStatement() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        doNothing().when(preparedStatement).setTimestamp(isA(int.class), isA(Timestamp.class));
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
        when(mockResultSet.getInt(MASTER_ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getInt(CLIENT_ID)).thenReturn(CLIENT_ID_VALUE);
        when(mockResultSet.getInt(CONTINUANCE)).thenReturn(CONTINUANCE_VALUE);
        when(mockResultSet.getTimestamp(DATE)).thenReturn(Timestamp.valueOf(DATE_VALUE));
        when(mockResultSet.getInt(PRICE)).thenReturn(PRICE_VALUE);
        when(mockResultSet.getInt(DISCOUNT)).thenReturn(ID_VALUE);
        when(mockResultSet.getString(STATUS)).thenReturn(AppointmentStatus.SUCCESS.name());
        when(mockResultSet.getString(PAYMENT_STATUS)).thenReturn(PaymentStatus.PAID_BY_CARD.name());
    }
}