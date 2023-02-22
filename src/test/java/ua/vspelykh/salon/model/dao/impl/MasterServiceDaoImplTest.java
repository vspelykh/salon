package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.model.dao.MasterServiceDao;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestMasterService;
import static ua.vspelykh.salon.model.dao.impl.SqlConstants.MasterService.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

class MasterServiceDaoImplTest extends AbstractDaoTest {

    private MasterServiceDao mockMasterServiceDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockMasterServiceDao = new MasterServiceDaoImpl();
        mockMasterServiceDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            MasterService testMasterService = getTestMasterService();
            testMasterService.setId(null);
            int id = mockMasterServiceDao.create(testMasterService);

            verifyQueryWithGeneratedKey(INSERT_MASTER_SERVICE);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setInt(++k, CONTINUANCE_VALUE);
            assertEquals(ID_VALUE, id);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void createWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent();
            assertThrows(DaoException.class, () -> mockMasterServiceDao.create(getTestMasterService()));

            verifyQueryWithGeneratedKey(INSERT_MASTER_SERVICE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockMasterServiceDao.update(getTestMasterService()));

            verifyQuery(UPDATE_MASTER_SERVICE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockMasterServiceDao.update(getTestMasterService()));

            verifyQuery(UPDATE_MASTER_SERVICE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void getAllByUserId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<MasterService> masterServices = mockMasterServiceDao.getAllByUserId(ID_VALUE);

            assertEquals(1, masterServices.size());
            assertEquals(getTestMasterService(), masterServices.get(0));

            verifyQuery(SELECT_SERVICES_BY_MASTER_ID);
        }
    }

    private PreparedStatement mockPrepareStatement() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
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
        when(mockResultSet.getInt(APPOINTMENT_ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getInt(MARK)).thenReturn(MARK_VALUE);
        when(mockResultSet.getString(COMMENT)).thenReturn(COMMENT_VALUE);
        when(mockResultSet.getTimestamp(DATE)).thenReturn(Timestamp.valueOf(DATE_VALUE));
    }
}