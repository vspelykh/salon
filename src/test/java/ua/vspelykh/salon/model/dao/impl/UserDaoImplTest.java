package ua.vspelykh.salon.model.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

class UserDaoImplTest extends AbstractDaoTest {

    private UserDao mockUserDao;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockUserDao = new UserDaoImpl();
        mockUserDao.setConnection(mockConnection);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void findById() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);

            mockResultSetIfPresent(mockResultSet);
            User user = mockUserDao.findById(getTestUser().getId());

            verifyQuery(SELECT_USER_BY_ID);
            verifyQuery(SELECT_USER_ROLE);
            verifyNoMoreInteractions(mockConnection);
            assertEquals(getTestUser(), user);
        }
    }

    @Test
    void findByIdNotFound() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);

            mockResultSetIfAbsent();

            assertThrows(DaoException.class, () -> mockUserDao.findById(ERROR_CODE));

            verifyQuery(SELECT_USER_BY_ID);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void findAll() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<User> users = mockUserDao.findAll();

            verifyQuery(SELECT_ALL_USERS);
            verifyQuery(SELECT_USER_ROLE);
            verifyNoMoreInteractions(mockConnection);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }

    @Test
    void findAllNotFound() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfAbsent();

            assertEquals(Collections.emptyList(), mockUserDao.findAll());

            verifyQuery(SELECT_ALL_USERS);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void removeById() throws SQLException {
        int i = 1;
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(i);
            assertDoesNotThrow(() -> mockUserDao.removeById(ID_VALUE));

            verifyQuery(DELETE_USER_BY_ID);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void removeByIdNotFound() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockUserDao.removeById(ID_VALUE));

            verifyQuery(DELETE_USER_BY_ID);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void findByRole() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<User> users = mockUserDao.findClients();

            verifyQuery(SELECT_USERS_BY_ROLE);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }

    @Test
    void updateRole() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockUserDao.updateRole(ID_VALUE, "add", Role.HAIRDRESSER));
            verifyQuery(INSERT_USER_ROLE);
        }
    }

    @Test
    @SuppressWarnings("MagicConstant")
    void create() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            User testUser = getTestUser();
            testUser.setId(null);
            int id = mockUserDao.create(testUser);

            verifyQueryWithGeneratedKey(INSERT_USER);
            verifyNoMoreInteractions(mockConnection);
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
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
            mockResultSetIfAbsent();
            assertThrows(DaoException.class, () -> mockUserDao.create(getTestUser()));

            verifyQueryWithGeneratedKey(INSERT_USER);
            verifyQuery(SELECT_USER_BY_ID);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void update() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(ID_VALUE);
            assertDoesNotThrow(() -> mockUserDao.update(getTestUser()));

            verifyQuery(UPDATE_USER);
            verifyQuery(SELECT_USER_BY_ID);
            verifyQuery(SELECT_USER_ROLE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void updateWithException() throws SQLException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeUpdate()).thenReturn(ERROR_CODE);
            assertThrows(DaoException.class, () -> mockUserDao.update(getTestUser()));

            verifyQuery(UPDATE_USER);
            verifyQuery(SELECT_USER_BY_ID);
            verifyQuery(SELECT_USER_ROLE);
            verifyNoMoreInteractions(mockConnection);
        }
    }

    @Test
    void findFiltered() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<User> users = mockUserDao.findFiltered(emptyList(), emptyList(), emptyList(), "", 1, 5,
                    MasterSort.NAME_ASC);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));
        }
    }

    @Test
    void findFilteredNotFound() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfAbsent();

            assertEquals(Collections.emptyList(), mockUserDao.findFiltered(emptyList(), emptyList(), emptyList(), "", 1,
                    5, MasterSort.NAME_ASC));
        }
    }

    @Test
    void getCountOfMasters() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            int count = mockUserDao.getCountOfMasters(emptyList(), emptyList(), emptyList(), "");

            assertEquals(1, count);
        }
    }

    @Test
    void findBySearch() throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            List<User> users = mockUserDao.findBySearch(EMAIL_VALUE);

            assertEquals(1, users.size());
            assertEquals(getTestUser(), users.get(0));

            verifyQuery(SELECT_USER_BY_SEARCH);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlCountTest")
    void getCountOfMastersCheckSql(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                                   String search, String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            mockUserDao.getCountOfMasters(levels, serviceIds, categoriesIds, search);
            verifyQuery(sqlExpected);
        }
    }

    @ParameterizedTest
    @MethodSource("dataForSqlTest")
    void findFilteredCheckSql(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                              String search, MasterSort sort, String sqlExpected) throws SQLException, DaoException {
        try (PreparedStatement statement = mockPrepareStatement(mockConnection)) {
            when(statement.executeQuery()).thenReturn(mockResultSet);
            mockResultSetIfPresent(mockResultSet);
            mockUserDao.findFiltered(levels, serviceIds, categoriesIds, search, 1, 5, sort);
            verifyQuery(sqlExpected);
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

        return Arrays.asList(new Object[][]{
                {emptyLevels, emptyServiceIds, emptyCategoriesIds, emptySearch, MasterSort.NAME_ASC, SELECT_FILTERED_1},
                {emptyLevels, emptyServiceIds, emptyCategoriesIds, emptySearch, MasterSort.RATING_ASC, SELECT_FILTERED_2},
                {levels, serviceIds, categoriesIds, search, MasterSort.NAME_DESC, SELECT_FILTERED_3},
                {levels, serviceIds, categoriesIds, search, MasterSort.RATING_DESC, SELECT_FILTERED_4},
                {emptyLevels, serviceIds, emptyCategoriesIds, search, MasterSort.FIRST_YOUNG, SELECT_FILTERED_5},
                {levels, emptyServiceIds, categoriesIds, emptySearch, MasterSort.FIRST_PRO, SELECT_FILTERED_6}
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
        doNothing().when(preparedStatement).setString(isA(int.class), isA(String.class));
        when(preparedStatement.execute()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(preparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(anyString())).thenReturn(Role.CLIENT.name());
        when(mockResultSet.getInt(1)).thenReturn(ID_VALUE);
        return preparedStatement;
    }
}