package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.junit.MockitoJUnitRunner;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static ua.vspelykh.salon.Constants.*;
import static ua.vspelykh.salon.model.dao.impl.DaoTestData.getTestUser;
import static ua.vspelykh.salon.model.dao.impl.SqlConstants.User.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

@RunWith(MockitoJUnitRunner.class)
class UserDaoImplTest {

    private Connection connection;
    private UserDao userDao;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() {
        connection = mock(Connection.class);
        userDao = new UserDaoImpl();
        userDao.setConnection(connection);
        resultSet = mock(ResultSet.class);
    }

    @Test
    void findById() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);

            mockResultSetIfPresent(resultSet);
            User user = userDao.findById(getTestUser().getId());

            verifySql(SELECT_USER_BY_ID);
            verifySql(SELECT_USER_ROLE);
            verifyNoMoreInteractions(connection);
            assertEquals(getTestUser(), user);
        }
    }

    @Test
    void findByIdNotFound() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);

            mockResultSetIfAbsent(resultSet);

            assertThrows(DaoException.class, () -> userDao.findById(ERROR_CODE));

            verifySql(SELECT_USER_BY_ID);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void findAll() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            List<User> users = userDao.findAll();

            verifySql(SELECT_ALL_USERS);
            verifySql(SELECT_USER_ROLE);
            verifyNoMoreInteractions(connection);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }

    @Test
    void findAllNotFound() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfAbsent(resultSet);

            assertEquals(Collections.emptyList(), userDao.findAll());

            verifySql(SELECT_ALL_USERS);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void removeById() throws SQLException {
        int i = 1;
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeUpdate()).thenReturn(i);
            assertDoesNotThrow(() -> userDao.removeById(ID_VALUE));

            verifySql(DELETE_USER_BY_ID);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void removeByIdNotFound() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> userDao.removeById(ID_VALUE));

            verifySql(DELETE_USER_BY_ID);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void findByRole() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            List<User> users = userDao.findClients();

            verifySql(SELECT_USERS_BY_ROLE);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }

    @Test
    void updateRole() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> userDao.updateRole(ID_VALUE, "add", Role.HAIRDRESSER));
            verifySql(INSERT_USER_ROLE);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            int id = userDao.create(getTestUser());

            verify(connection).prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            verifySql(SELECT_USER_BY_ID);
            verifySql(SELECT_USER_ROLE);
            verifyNoMoreInteractions(connection);
            verify(statement).setString(1, NAME_VALUE);
            verify(statement).setString(2, SURNAME_VALUE);
            verify(statement).setString(3, EMAIL_VALUE);
            verify(statement).setString(4, NUMBER_VALUE);
            verify(statement).setString(eq(5), anyString());
            verify(statement).getGeneratedKeys();
            assertEquals(ID_VALUE, id);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void createWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent(resultSet);
            assertThrows(DaoException.class, () -> userDao.create(getTestUser()));

            verify(connection).prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            verifySql(SELECT_USER_BY_ID);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> userDao.update(getTestUser()));

            verifySql(UPDATE_USER);
            verifySql(SELECT_USER_BY_ID);
            verifySql(SELECT_USER_ROLE);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> userDao.update(getTestUser()));

            verifySql(UPDATE_USER);
            verifySql(SELECT_USER_BY_ID);
            verifySql(SELECT_USER_ROLE);
            verifyNoMoreInteractions(connection);
        }
    }

    @Test
    void findFiltered() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            List<User> users = userDao.findFiltered(emptyList(), emptyList(), emptyList(), "", 1, 5,
                    MasterSort.NAME_ASC);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }

    @Test
    void findFilteredNotFound() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfAbsent(resultSet);

            assertEquals(Collections.emptyList(), userDao.findFiltered(emptyList(), emptyList(), emptyList(), "", 1,
                    5, MasterSort.NAME_ASC));
        }
    }

    @Test
    void getCountOfMasters() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            int count = userDao.getCountOfMasters(emptyList(), emptyList(), emptyList(), "");

            assertEquals(1, count);
        }
    }

    @Test
    void findBySearch() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            List<User> users = userDao.findBySearch(EMAIL_VALUE);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));

            verifySql(SELECT_USER_BY_SEARCH);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlCountTest")
    void getCountOfMastersCheckSql(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                                   String search, String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            userDao.getCountOfMasters(levels, serviceIds, categoriesIds, search);
            verifySql(sqlExpected);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlTest")
    void findFilteredCheckSql(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                              String search, int page, int size, MasterSort sort, String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(connection)) {
            when(statement.executeQuery()).thenReturn(resultSet);
            mockResultSetIfPresent(resultSet);
            userDao.findFiltered(levels, serviceIds, categoriesIds, search, page, size, sort);
            verify(connection).prepareStatement(sqlExpected);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> dataForSqlTest() {
        List<MastersLevel> emptyLevels = Collections.emptyList();
        List<Integer> emptyServiceIds = Collections.emptyList();
        List<Integer> emptyCategoriesIds = Collections.emptyList();

        List<MastersLevel> levels = List.of(MastersLevel.YOUNG);
        List<Integer> serviceIds = Arrays.asList(1, 2, 3);
        List<Integer> categoriesIds = Arrays.asList(4, 5, 6);

        String emptySearch = "";
        String search = "Anna";

        int page = 1;
        int size = 5;

        return Arrays.asList(new Object[][]{
                {emptyLevels, emptyServiceIds, emptyCategoriesIds, emptySearch, page, size, MasterSort.NAME_ASC, SELECT_FILTERED_1},
                {emptyLevels, emptyServiceIds, emptyCategoriesIds, emptySearch, page, size, MasterSort.RATING_ASC, SELECT_FILTERED_2},
                {levels, serviceIds, categoriesIds, search, page, size, MasterSort.NAME_DESC, SELECT_FILTERED_3},
                {levels, serviceIds, categoriesIds, search, page, size, MasterSort.RATING_DESC, SELECT_FILTERED_4},
                {emptyLevels, serviceIds, emptyCategoriesIds, search, page, size, MasterSort.FIRST_YOUNG, SELECT_FILTERED_5},
                {levels, emptyServiceIds, categoriesIds, emptySearch, page, size, MasterSort.FIRST_PRO, SELECT_FILTERED_6}
        });
    }

    @Parameterized.Parameters
    public static Collection<Object[]> dataForSqlCountTest() {
        List<MastersLevel> emptyLevels = Collections.emptyList();
        List<Integer> emptyServiceIds = Collections.emptyList();
        List<Integer> emptyCategoriesIds = Collections.emptyList();

        List<MastersLevel> levels = List.of(MastersLevel.YOUNG);
        List<Integer> serviceIds = Arrays.asList(1, 2, 3);
        List<Integer> categoriesIds = Arrays.asList(4, 5, 6);

        String emptySearch = "";
        String search = "Anna";

        return Arrays.asList(new Object[][]{
                {emptyLevels, emptyServiceIds, emptyCategoriesIds, emptySearch, SELECT_COUNT_1},
                {levels, serviceIds, categoriesIds, search, SELECT_COUNT_2},
                {emptyLevels, serviceIds, emptyCategoriesIds, search, SELECT_COUNT_3},
        });
    }

    private void verifySql(String sql) throws SQLException {
        verify(connection).prepareStatement(sql);
    }

    private void mockResultSetIfAbsent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(false);
    }

    private void mockResultSetIfPresent(ResultSet resultSet) throws SQLException {
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(ID)).thenReturn(ID_VALUE);
        when(resultSet.getString(NAME)).thenReturn(NAME_VALUE);
        when(resultSet.getString(SURNAME)).thenReturn(SURNAME_VALUE);
        when(resultSet.getString(EMAIL)).thenReturn(EMAIL_VALUE);
        when(resultSet.getString(NUMBER)).thenReturn(NUMBER_VALUE);
        when(resultSet.getString(PASSWORD)).thenReturn(PASSWORD_VALUE);
        when(resultSet.getString(ROLE)).thenReturn(Role.CLIENT.name());
    }

    private PreparedStatement mockPrepareStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(isA(String.class))).thenReturn(preparedStatement);
        doNothing().when(preparedStatement).setInt(isA(int.class), isA(int.class));
        doNothing().when(preparedStatement).setLong(isA(int.class), isA(long.class));
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        doNothing().when(preparedStatement).setObject(isA(int.class), isA(Object.class));
        when(preparedStatement.execute()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        when(resultSet.getString(anyString())).thenReturn(Role.CLIENT.name());
        when(resultSet.getInt(1)).thenReturn(ID_VALUE);
        return preparedStatement;
    }
}