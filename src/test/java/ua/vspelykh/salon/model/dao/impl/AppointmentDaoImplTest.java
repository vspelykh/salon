package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
class AppointmentDaoImplTest {

    private Connection mockConnection;
    private AppointmentDao mockAppointmentDao;
    private ResultSet mockResultSet;

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
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            Appointment testAppointment = getTestAppointment();
            testAppointment.setId(null);
            int id = mockAppointmentDao.create(testAppointment);

            verify(mockConnection).prepareStatement("INSERT INTO appointments (master_id, client_id, continuance, date, price, discount, status, payment_status) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent(mockResultSet);
            assertThrows(DaoException.class, () -> mockAppointmentDao.create(getTestAppointment()));

            verify(mockConnection).prepareStatement("INSERT INTO appointments (master_id, client_id, continuance, date, price, discount, status, payment_status) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockAppointmentDao.update(getTestAppointment()));

            verifySql("UPDATE appointments SET master_id = ?, client_id = ?, continuance = ?, date = ?, price = ?, discount = ?, status = ?, payment_status = ? WHERE id = ?");
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockAppointmentDao.update(getTestAppointment()));

            verifySql("UPDATE appointments SET master_id = ?, client_id = ?, continuance = ?, date = ?, price = ?, discount = ?, status = ?, payment_status = ? WHERE id = ?");
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void getByDateAndMasterId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<Appointment> appointments = mockAppointmentDao.getByDateAndMasterId(LocalDate.now(), ID_VALUE);

            assertEquals(1, appointments.size());
            assertEquals(getTestAppointment(), appointments.get(0));

            verifySql(SELECT_BY_DATE_AND_ID);
        }
    }

    @Test
    void getAllByDate() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<Appointment> appointments = mockAppointmentDao.getAllByDate(LocalDate.now());

            assertEquals(1, appointments.size());
            assertEquals(getTestAppointment(), appointments.get(0));
        }
    }

    @Test
    void getAllByDateNotFound() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfAbsent(mockResultSet);

            assertEquals(Collections.emptyList(), mockAppointmentDao.getAllByDate(LocalDate.now()));
        }
    }

    @Test
    void getFiltered() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<Appointment> appointments = mockAppointmentDao.getFiltered(ID_VALUE, DATE_VALUE.toLocalDate(),
                    DATE_VALUE.toLocalDate(), AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD, 5, 1);

            assertEquals(1, appointments.size());
            assertEquals(getTestAppointment(), appointments.get(0));
        }
    }

    @Test
    void getCountOfAppointments() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            int count = mockAppointmentDao.getCountOfAppointments(ID_VALUE, DATE_VALUE.toLocalDate(),
                    DATE_VALUE.toLocalDate(), AppointmentStatus.SUCCESS, PaymentStatus.PAID_BY_CARD);

            assertEquals(1, count);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlTest")
    void getFilteredCheckSql(LocalDate from, LocalDate to, AppointmentStatus status, PaymentStatus paymentStatus,
                             String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            mockAppointmentDao.getFiltered(ID_VALUE, from, to, status, paymentStatus, 1, 5);
            verifySql(sqlExpected);
        }
    }



    @ParameterizedTest
    @MethodSource("dataForSqlCountTest")
    void getCountOfAppointmentsCheckSql(LocalDate from, LocalDate to, AppointmentStatus status, PaymentStatus paymentStatus,
                                        String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
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

    private void verifySql(String sql) throws SQLException {
        verify(mockConnection).prepareStatement(sql);
    }

    private PreparedStatement mockPrepareStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
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

    private void mockResultSetIfPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(ID)).thenReturn(ID_VALUE);
        when(resultSet.getInt(MASTER_ID)).thenReturn(ID_VALUE);
        when(resultSet.getInt(CLIENT_ID)).thenReturn(CLIENT_ID_VALUE);
        when(resultSet.getInt(CONTINUANCE)).thenReturn(CONTINUANCE_VALUE);
        when(resultSet.getTimestamp(DATE)).thenReturn(Timestamp.valueOf(DATE_VALUE));
        when(resultSet.getInt(PRICE)).thenReturn(PRICE_VALUE);
        when(resultSet.getInt(DISCOUNT)).thenReturn(ID_VALUE);
        when(resultSet.getString(STATUS)).thenReturn(AppointmentStatus.SUCCESS.name());
        when(resultSet.getString(PAYMENT_STATUS)).thenReturn(PaymentStatus.PAID_BY_CARD.name());
    }

    private void mockResultSetIfAbsent(ResultSet mockResultSet) throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
    }

}