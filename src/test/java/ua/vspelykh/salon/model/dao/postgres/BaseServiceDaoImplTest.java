package ua.vspelykh.salon.model.dao.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.model.dao.postgres.DaoTestData.getTestBaseService;
import static ua.vspelykh.salon.model.dao.postgres.SqlConstants.BasService.*;

class BaseServiceDaoImplTest extends AbstractDaoTest {

    private BaseServiceDao mockBaseServiceDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockBaseServiceDao = new BaseServiceDaoImpl();
        mockBaseServiceDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            int id = mockBaseServiceDao.create(getTestBaseService());

            verifyQueryWithGeneratedKey(INSERT_BASE_SERVICE);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setString(++k, SERVICE_VALUE);
            verify(statement).setString(++k, SERVICE_UA_VALUE);
            verify(statement).setInt(++k, PRICE_VALUE);
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
            assertThrows(DaoException.class, () -> mockBaseServiceDao.create(getTestBaseService()));

            verifyQueryWithGeneratedKey(INSERT_BASE_SERVICE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockBaseServiceDao.update(getTestBaseService()));

            verifyQuery(UPDATE_BASE_SERVICE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockBaseServiceDao.update(getTestBaseService()));

            verifyQuery(UPDATE_BASE_SERVICE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void findByFilter() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            List<BaseService> baseServices = mockBaseServiceDao.findByFilter(List.of(1), 1, 5);

            assertEquals(1, baseServices.size());
            assertEquals(getTestBaseService(), baseServices.get(0));
            verifyQuery(SELECT_BASE_SERVICES_1);
            mockBaseServiceDao.findByFilter(Collections.emptyList(), 1, 5);
            verifyQuery(SELECT_BASE_SERVICES_2);
        }
    }

    @Test
    void getCountOfCategories() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            int count = mockBaseServiceDao.getCountOfCategories(List.of(1));

            assertEquals(1, count);
            verifyQuery(SELECT_COUNT_1);
        }
    }

    @Test
    void getCountOfCategoriesWithEmptyList() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            int count = mockBaseServiceDao.getCountOfCategories(Collections.emptyList());

            assertEquals(1, count);
            verifyQuery(SELECT_COUNT_2);
        }
    }

    private void mockResultSetIfPresent() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getInt(CATEGORY_ID)).thenReturn(ID_VALUE);
        when(mockResultSet.getString(SERVICE)).thenReturn(SERVICE_VALUE);
        when(mockResultSet.getString(SERVICE + UA)).thenReturn(SERVICE_UA_VALUE);
        when(mockResultSet.getInt(PRICE)).thenReturn(PRICE_VALUE);
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
}