package ua.vspelykh.salon.model.dao.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.ID_VALUE;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestAppointmentItem;
import static ua.vspelykh.salon.model.dao.postgres.SqlConstants.AppointmentItems.INSERT_APPOINTMENT_ITEM;

class AppointmentItemDaoImplTest extends AbstractDaoTest {

    private AppointmentItemDao mockAppointmentItemDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockAppointmentItemDao = new AppointmentItemDaoImpl();
        mockAppointmentItemDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            int id = mockAppointmentItemDao.create(getTestAppointmentItem());

            verifyQueryWithGeneratedKey(INSERT_APPOINTMENT_ITEM);
            verifyNoMoreInteractions(mockConnection);

            verify(statement).setInt(1, ID_VALUE);
            verify(statement).setInt(2, ID_VALUE);
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
            assertThrows(DaoException.class, () -> mockAppointmentItemDao.create(getTestAppointmentItem()));

            verifyQueryWithGeneratedKey(INSERT_APPOINTMENT_ITEM);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() {
        AppointmentItem testAppointmentItem = getTestAppointmentItem();
        assertThrows(UnsupportedOperationException.class, () -> mockAppointmentItemDao.update(testAppointmentItem));
    }

    private PreparedStatement mockPrepareStatement() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        when(preparedStatement.execute()).thenReturn(true);
        when(mockResultSet.next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(preparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getInt(1)).thenReturn(ID_VALUE);
        return preparedStatement;
    }
}