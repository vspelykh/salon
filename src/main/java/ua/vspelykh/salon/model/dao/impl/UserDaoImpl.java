package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static ua.vspelykh.salon.controller.ControllerConstants.ADD;
import static ua.vspelykh.salon.controller.ControllerConstants.REMOVE;
import static ua.vspelykh.salon.model.dao.Table.*;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.validation.Validation.checkPassword;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {
        super(RowMapperFactory.getUserRowMapper(), Table.USER);
    }

    @Override
    public User findById(int id) throws DaoException {
        return setUserRoles(super.findById(id));
    }

    @Override
    public List<User> findAll() throws DaoException {
        return setUserRoles(super.findAll());
    }

    @Override
    public int create(User entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, NAME, SURNAME, EMAIL, NUMBER, PASSWORD).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            encryptPassword(entity);
            setUserStatement(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException(NO_ID + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_CREATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public void update(User entity) throws DaoException {
        String query = new QueryBuilder().update(tableName).set(NAME, SURNAME, EMAIL, NUMBER, PASSWORD).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            encryptPassword(entity);
            setUserStatement(entity, statement);
            statement.setInt(6, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    private void encryptPassword(User user) throws DaoException {
        if (user.isNew() || !findById(user.getId()).getPassword().equals(user.getPassword())) {
            BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
            try {
                checkPassword(user.getPassword());
            } catch (ServiceException e) {
                throw new DaoException(PASSWORD);
            }
            user.setPassword(encryptor.encryptPassword(user.getPassword()));
        }
    }

    @Override
    public User findByNumber(String number) throws DaoException {
        return findByParam(number, Column.NUMBER);
    }

    @Override
    public User findByEmail(String email) throws DaoException {
        return setUserRoles(findByParam(email, Column.EMAIL));
    }

    @Override
    public List<User> findClients() throws DaoException {
        return findByRole(Role.CLIENT);
    }

    @Override
    public List<User> findMasters() throws DaoException {
        return findByRole(Role.HAIRDRESSER);
    }

    @Override
    public List<User> findFiltered(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                                   String search, int page, int size, MasterSort sort) throws DaoException {
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(levels, serviceIds, categoriesIds,
                page, size, sort, search);
        String query;
        if (sort.equals(MasterSort.RATING_ASC) || sort.equals(MasterSort.RATING_DESC)) {
            query = queryBuilder.buildQueryWithRatingSorting();
        } else {
            query = queryBuilder.buildQuery();
        }
        return getUsersFromDB(queryBuilder, query);
    }

    @Override
    public int getCountOfMasters(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds, String search) throws DaoException {
        int count;
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(levels, serviceIds, categoriesIds, search);
        String query = queryBuilder.buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            } else {
                LOG.error(String.format("%s%s", FAIL_COUNT, tableName));
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
        return count;
    }

    @Override
    public List<User> findBySearch(String search) throws DaoException {
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(search);
        String query = queryBuilder.buildSearchQuery();
        return setUserRoles(getUsersFromDB(queryBuilder, query));
    }

    @Override
    public void updateRole(int userId, String action, Role role) throws DaoException {
        String query;
        if (ADD.equals(action)) {
            query = new QueryBuilder().insert(USER_ROLES, USER_ID, ROLE).build();
        } else if (REMOVE.equals(action)) {
            query = new QueryBuilder().delete(USER_ROLES).where(USER_ID).and(ROLE).build();
        } else throw new DaoException();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            setUserRoleStatement(statement, userId, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(String.format("Fail to update role. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }


    private void setUserRoleStatement(PreparedStatement statement, int userId, Role role) throws SQLException {
        int k = 0;
        statement.setInt(++k, userId);
        statement.setString(++k, String.valueOf(role));
    }

    private List<User> getUsersFromDB(MasterFilteredQueryBuilder queryBuilder, String query) throws DaoException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User entity = rowMapper.map(resultSet);
                users.add(entity);
            }
            return users;
        } catch (SQLException e) {
            LOG.error(String.format("Fail to get users from DB. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }

    private List<User> findByRole(Role role) throws DaoException {
        String query = new QueryBuilder().select(tableName).alias(tableName).innerJoin(USER_ROLES, "u.id=ur.user_id")
                .andWithAlias(USER_ROLES, ROLE).build();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, role.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = rowMapper.map(resultSet);
                setUserRoles(user);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            LOG.error(String.format("%sby role. Issue: %s", FAIL_FIND_LIST, e.getMessage()));
            throw new DaoException(e);
        }
    }

    private void setUserStatement(User entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getSurname());
        statement.setString(++k, entity.getEmail());
        statement.setString(++k, entity.getNumber());
        statement.setString(++k, entity.getPassword());
    }

    private User setUserRoles(User user) throws DaoException {
        String query = new QueryBuilder().selectFields(USER_ROLES, ROLE).where(USER_ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<Role> roles = user.getRoles();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString(Column.ROLE)));
            }
            return user;
        } catch (SQLException e) {
            LOG.error(String.format("Error during setting roles for user. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }

    private List<User> setUserRoles(List<User> users) throws DaoException {
        for (User user : users) {
            setUserRoles(user);
        }
        return users;
    }

    private class MasterFilteredQueryBuilder extends QueryBuilder {

        private static final String COUNT_MASTERS_QUERY = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul " +
                "ON u.id = ul.id";
        private static final String SELECT_USERS = "SELECT u.id, name, surname, email, number, password, AVG(coalesce(mark,0)) " +
                "as average FROM users u INNER JOIN user_level ul ON u.id = ul.id LEFT JOIN feedbacks m ON u.id=" +
                "(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id)";

        private final List<MastersLevel> levels;
        private final List<Integer> serviceIds;
        private List<Integer> categoriesIds;
        private int page;
        private int size;
        private MasterSort sort;
        private final String search;

        private boolean isSearch = false;

        public MasterFilteredQueryBuilder(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds,
                                          int page, int size, MasterSort sort, String search) {
            this.levels = levels;
            this.serviceIds = serviceIds;
            this.categoriesIds = categoriesIds;
            this.page = page;
            this.size = size;
            this.sort = sort;
            this.search = search;
        }

        public MasterFilteredQueryBuilder(List<MastersLevel> levels, List<Integer> serviceIds,
                                          List<Integer> categoriesIds, String search) {
            this.levels = levels;
            this.serviceIds = serviceIds;
            this.categoriesIds = categoriesIds;
            this.search = search;
        }

        public MasterFilteredQueryBuilder(String search) {
            this.search = search;
            levels = Collections.emptyList();
            serviceIds = Collections.emptyList();
            isSearch = true;
        }

        private String buildQuery() {
            select(tableName).alias(tableName);
            if (isAllListsAndSearchAreEmpty()) {
                return shortQuery();
            } else {
                innerJoin(USER_LEVEL, "u.id=ul.id ");
                appendLevels().appendServices().appendSearch(WHERE);
                if (search != null && !search.isEmpty()) {
                    and(ACTIVE);
                } else {
                    where(ACTIVE);
                }
                return orderBy(sort).pagination(page, size).build();
            }
        }

        private String buildQueryWithRatingSorting() {
            append(SELECT_USERS);
            appendServices().appendLevelsForRatingQuery();
            append(" GROUP BY u.id ");
            appendSearch(HAVING);
            append(" ORDER BY average");
            if (sort.equals(MasterSort.RATING_DESC)) {
                desc();
            }
            return pagination(page, size).build();
        }

        private String buildSearchQuery() {
            return select(tableName).whereILike(NUMBER)
                    .orILike(EMAIL).build();
        }

        private String shortQuery() {
            innerJoin(USER_LEVEL, "u.id=ul.id").where("ul.active");
            return orderBy(sort).pagination(page, size).build();
        }

        private String buildCountQuery() {
            append(COUNT_MASTERS_QUERY);
            if (isAllListsAndSearchAreEmpty()) {
                return where(ACTIVE).build();
            }
            appendLevels().appendServices().appendSearch(WHERE);
            if (search != null && !search.isEmpty()) {
                and(ACTIVE);
            } else {
                where(ACTIVE);
            }
            return build();
        }

        private MasterFilteredQueryBuilder appendServices() {
            if (!serviceIds.isEmpty()) {
                innerJoinCondition("SELECT master_id from master_services ms");
                query.replace(query.length() - 1, query.length(), "");
                whereAliasIn(MASTER_SERVICES, BASE_SERVICE_ID, serviceIds.size());
                if (!categoriesIds.isEmpty()) {
                    query.append(AND);
                }
                appendCategories();
                query.append(" GROUP BY ms.master_id) AS q ON q.master_id = u.id");
            } else if (!categoriesIds.isEmpty()) {
                innerJoinCondition("SELECT master_id from master_services ms");
                query.replace(query.length() - 1, query.length(), "");
                append(WHERE);
                appendCategories();
                query.append(" GROUP BY ms.master_id) AS q ON q.master_id = u.id");
            }
            return this;
        }

        private MasterFilteredQueryBuilder appendLevels() {
            if (!levels.isEmpty()) {
                andInCondition(USER_LEVEL, LEVEL, levels.size());
            }
            return this;
        }

        private void appendCategories() {
            if (!categoriesIds.isEmpty()) {
                String condition = new QueryBuilder().selectFields(BASE_SERVICE, ID)
                        .whereIn(CATEGORY_ID, categoriesIds.size()).build();
                aliasIn(MASTER_SERVICES, BASE_SERVICE_ID, condition);
            }
        }

        private boolean isAllListsAndSearchAreEmpty() {
            return levels.isEmpty() && serviceIds.isEmpty() && (search == null || search.isEmpty())
                    && (categoriesIds == null || categoriesIds.isEmpty());
        }

        private void setParams(PreparedStatement preparedStatement) throws SQLException {
            int paramNum = 1;
            if (isSearch) {
                setSearch(preparedStatement, paramNum);
            } else if (MasterSort.RATING_ASC.equals(sort) || MasterSort.RATING_DESC.equals(sort)) {
                paramNum = setServices(preparedStatement, paramNum);
                paramNum = setCategories(preparedStatement, paramNum);
                paramNum = setLevels(preparedStatement, paramNum);
                preparedStatement.setBoolean(paramNum, true);
                paramNum++;
                setSearch(preparedStatement, paramNum);

            } else {
                paramNum = setLevels(preparedStatement, paramNum);
                paramNum = setServices(preparedStatement, paramNum);
                paramNum = setCategories(preparedStatement, paramNum);
                paramNum = setSearch(preparedStatement, paramNum);
                preparedStatement.setBoolean(paramNum, true);
            }
        }

        private int setCategories(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (categoriesIds != null && !categoriesIds.isEmpty()) {
                for (Integer id : categoriesIds) {
                    preparedStatement.setInt(paramNum++, id);
                }
            }
            return paramNum;
        }

        private int setSearch(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (search != null && !search.isEmpty()) {
                String[] strings = search.split("[ ]+");
                for (String s : strings) {
                    preparedStatement.setString(paramNum++, String.format(SEARCH_PATTERN, s));
                }
                for (String s : strings) {
                    preparedStatement.setString(paramNum++, String.format(SEARCH_PATTERN, s));
                }
            }
            return paramNum;
        }

        private int setLevels(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (levels != null && !levels.isEmpty()) {
                for (MastersLevel level : levels) {
                    preparedStatement.setString(paramNum++, level.name());
                }
            }
            return paramNum;
        }

        private int setServices(PreparedStatement preparedStatement, int paramNum) throws SQLException {
            if (serviceIds != null && !serviceIds.isEmpty()) {
                for (Integer serviceId : serviceIds) {
                    preparedStatement.setInt(paramNum++, serviceId);
                }
            }
            return paramNum;
        }

        private void appendLevelsForRatingQuery() {
            if (!levels.isEmpty()) {
                whereAliasIn(USER_LEVEL, LEVEL, levels.size());
                and(ACTIVE);
            } else {
                where(ACTIVE);
            }
        }

        private MasterFilteredQueryBuilder appendSearch(String key) {
            if (search != null && !search.isEmpty()) {
                String[] strings = search.split("[ ]+");
                query.append(key);
                Arrays.stream(strings).forEach(s -> iLike(NAME).append(OR));
                Arrays.stream(strings).forEach(s -> iLike(SURNAME).append(OR));
                query.replace(query.length() - 4, query.length(), "");
            }
            return this;
        }
    }
}
