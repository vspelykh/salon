package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.vspelykh.salon.model.dao.UserLevelDao;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUserLevel;
import static ua.vspelykh.salon.model.dao.impl.SqlConstants.UserLevel.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;


class UserLevelDaoImplTest extends AbstractDaoTest {

    private UserLevelDao mockUserLevelDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockUserLevelDao = new UserLevelDaoImpl();
        mockUserLevelDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(eq(INSERT_USER_LEVEL), anyInt())).thenReturn(statement);
            int id = mockUserLevelDao.create(getTestUserLevel());

            verifyQueryWithGeneratedKey(INSERT_USER_LEVEL);
            verifyNoMoreInteractions(mockConnection);

            int k = 0;
            verify(statement).setInt(++k, ID_VALUE);
            verify(statement).setString(++k, MastersLevel.YOUNG.name());
            verify(statement).setBoolean(++k, true);
            verify(statement).setString(++k, ABOUT_VALUE);
            verify(statement).setString(++k, ABOUT_UA_VALUE);
            verify(statement).getGeneratedKeys();
            assertEquals(ID_VALUE, id);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void createWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(mockConnection.prepareStatement(eq(INSERT_USER_LEVEL), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent();
            assertThrows(DaoException.class, () -> mockUserLevelDao.create(getTestUserLevel()));

            verifyQueryWithGeneratedKey(INSERT_USER_LEVEL);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockUserLevelDao.update(getTestUserLevel()));

            verifyQuery(UPDATE_USER_LEVEL);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockUserLevelDao.update(getTestUserLevel()));

            verifyQuery(UPDATE_USER_LEVEL);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void getUserLevelByUserId() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            UserLevel userLevel = mockUserLevelDao.getUserLevelByUserId(ID_VALUE);

            assertEquals(getTestUserLevel(), userLevel);

            verifyQuery(SELECT_USER_LEVEL_BY_ID);
        }
    }

    @Test
    void getUserLevelByUserIdWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfAbsent();

            assertThrows(DaoException.class, () -> mockUserLevelDao.getUserLevelByUserId(ID_VALUE));

            verifyQuery(SELECT_USER_LEVEL_BY_ID);
        }
    }

    @Test
    void isExist() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent();
            boolean isExist = mockUserLevelDao.isExist(ID_VALUE);

            assertTrue(isExist);

            verifyQuery(SELECT_USER_LEVEL_EXISTS);
        }
    }


    @Test
    void isExistWhenIsNotExist() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement()) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfAbsent();

            assertThrows(DaoException.class, () -> mockUserLevelDao.isExist(ID_VALUE));
            verifyQuery(SELECT_USER_LEVEL_EXISTS);
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
        when(mockResultSet.getString(LEVEL)).thenReturn(MastersLevel.YOUNG.name());
        when(mockResultSet.getString(ABOUT)).thenReturn(ABOUT_VALUE);
        when(mockResultSet.getString(ABOUT + UA)).thenReturn(ABOUT_UA_VALUE);
        when(mockResultSet.getBoolean(1)).thenReturn(true);
    }
}