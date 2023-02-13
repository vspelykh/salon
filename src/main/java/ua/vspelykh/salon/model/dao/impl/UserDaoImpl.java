package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.model.dao.AbstractDao;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ua.vspelykh.salon.controller.command.user.ChangeRoleCommand.ADD;
import static ua.vspelykh.salon.controller.command.user.ChangeRoleCommand.REMOVE;
import static ua.vspelykh.salon.model.dao.Table.USER_LEVEL;
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
    public int create(User entity) throws DaoException {
        String query = INSERT + tableName + " (name, surname, email, number, password)" + VALUES + "(?,?,?,?,?)";
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
        String query = "UPDATE users SET name = ?, surname = ?, email = ?, number = ?, password = ? WHERE id = ?";
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
    public User findByEmailAndPassword(String email, String password) throws DaoException {
        User user;
        String query = SELECT + tableName + " WHERE email=? AND password=?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = rowMapper.map(resultSet);
            } else {
                LOG.error("No entity from {} found by {} with password.", tableName, email);
                throw new DaoException("No entity from " + tableName + " found by " + email + " with password.");
            }
        } catch (SQLException e) {
            LOG.error(String.format("%s %s by email and password", FAIL_FIND, tableName));
            throw new DaoException(e);
        }
        return setUserRoles(user);
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
    public List<User> findAdministrators() throws DaoException {
        return findByRole(Role.ADMINISTRATOR);
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
                LOG.error(FAIL_COUNT + tableName);
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
            query = ADD_ROLE_QUERY;
        } else if (REMOVE.equals(action)) {
            query = UPDATE_ROLE_QUERY;
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
        String query = SELECT + "users u INNER JOIN user_roles ur ON u.id = ur.user_id AND ur.role=?";
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
        String query = "SELECT role FROM user_roles WHERE user_id=?";
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

    private class MasterFilteredQueryBuilder {

        private final List<MastersLevel> levels;
        private final List<Integer> serviceIds;
        private List<Integer> categoriesIds;
        private int page;
        private int size;
        private MasterSort sort;
        private final String search;

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
        }

        private String buildQuery() {
            StringBuilder query = new StringBuilder(SELECT + tableName).append(" u");
            if (isAllListsAndSearchAreEmpty()) {
                return shortQuery();
            } else {
                query.append(INNER_JOIN).append(USER_LEVEL).append(" ").append("ul ");
                query.append("ON u.id=ul.id ");
                appendLevels(query);
                appendServices(query);
                appendSearch(query, WHERE);
                if (search != null && !search.isEmpty()) {
                    query.append(AND);
                } else {
                    query.append(WHERE);
                }
                query.append(ACTIVE_PARAM);
            }
            return addPagingParams(query);
        }

        private void appendServices(StringBuilder query) {
            if (!serviceIds.isEmpty()) {
                query.append(INNER_JOIN).append("(SELECT master_id from master_services s").append(WHERE);
                query.append("s.base_service_id IN(");
                appendQuestionMarks(query, serviceIds);
                if (!categoriesIds.isEmpty()) {
                    query.append(AND);
                }
                appendCategories(query);
                query.append(" GROUP BY s.master_id) AS q ON q.master_id = u.id");
            } else if (!categoriesIds.isEmpty()) {
                query.append(INNER_JOIN).append("(SELECT master_id from master_services s").append(WHERE);
                appendCategories(query);
                query.append(" GROUP BY s.master_id) AS q ON q.master_id = u.id");
            }
        }

        private void appendLevels(StringBuilder query) {
            if (!levels.isEmpty()) {
                query.append("and ul.level IN(");
                appendQuestionMarks(query, levels);
            }
        }

        private void appendCategories(StringBuilder q) {
            if (!categoriesIds.isEmpty()) {
                q.append(" s.base_service_id IN(SELECT id from base_services WHERE category_id IN(");
                appendQuestionMarks(q, categoriesIds);
                q.append(")");
            }
        }

        private String buildCountQuery() {
            StringBuilder query = new StringBuilder(COUNT_MASTERS_QUERY);
            if (isAllListsAndSearchAreEmpty()) {
                query.append(WHERE).append(ACTIVE_PARAM);
                return query.toString();
            }
            appendLevels(query);
            appendServices(query);
            appendSearch(query, WHERE);
            if (search != null && !search.isEmpty()) {
                query.append(AND);
            } else {
                query.append(WHERE);
            }
            query.append(ACTIVE_PARAM);
            return query.toString();
        }

        private String buildSearchQuery() {
            return SELECT + tableName + WHERE +
                    NUMBER + ILIKE + String.format(SEARCH_PATTERN, search) + OR +
                    EMAIL + ILIKE + String.format(SEARCH_PATTERN, search);
        }

        private String shortQuery() {
            StringBuilder q = new StringBuilder(SELECT + tableName + " u" + INNER_JOIN + "user_level ul ON u.id = ul.id");
            q.append(WHERE).append("ul.active='true'");
            return addPagingParams(q);
        }

        private String addPagingParams(StringBuilder q) {
            addSortingParams(q);
            return getQuery(q);
        }

        private String addingPagingParamsWithoutSorting(StringBuilder q) {
            return getQuery(q);
        }

        private String getQuery(StringBuilder q) {
            int offset;
            if (page == 1) {
                offset = 0;
            } else {
                offset = (page - 1) * size;
            }
            q.append(LIMIT).append(size);
            q.append(OFFSET).append(offset);
            return q.toString();
        }

        private void addSortingParams(StringBuilder q) {
            q.append(ORDER_BY);
            if (sort == MasterSort.NAME_ASC) {
                q.append(NAME_ASC);
            } else if (sort == MasterSort.NAME_DESC) {
                q.append(NAME_DESC);
            } else if (sort == MasterSort.FIRST_PRO) {
                q.append(LEVEL_EXP);
            } else if (sort == MasterSort.FIRST_YOUNG) {
                q.append(LEVEL_YOUNG);
            }
        }

        private void appendQuestionMarks(StringBuilder query, List<?> userIds) {
            for (int i = 0; i < userIds.size(); i++) {
                query.append("?");
                if (i != userIds.size() - 1)
                    query.append(",");
            }
            query.append(")");
        }

        private boolean isAllListsAndSearchAreEmpty() {
            return levels.isEmpty() && serviceIds.isEmpty() && (search == null || search.isEmpty())
                    && (categoriesIds == null || categoriesIds.isEmpty());
        }

        private void setParams(PreparedStatement preparedStatement) throws SQLException {
            int paramNum = 1;
            if (MasterSort.RATING_ASC.equals(sort) || MasterSort.RATING_DESC.equals(sort)) {
                paramNum = setServices(preparedStatement, paramNum);
                paramNum = setCategories(preparedStatement, paramNum);
                setLevels(preparedStatement, paramNum);
            } else {
                paramNum = setLevels(preparedStatement, paramNum);
                paramNum = setServices(preparedStatement, paramNum);
                setCategories(preparedStatement, paramNum);
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

        private String buildQueryWithRatingSorting() {
            StringBuilder q = new StringBuilder(SELECT_USERS);
            appendServices(q);
            appendLevelsForRatingQuery(q);
            q.append(" GROUP BY u.id ");
            appendSearch(q, HAVING);
            q.append("ORDER BY average");
            if (sort.equals(MasterSort.RATING_DESC)) {
                q.append(" desc");
            }
            return addingPagingParamsWithoutSorting(q);
        }

        private void appendLevelsForRatingQuery(StringBuilder q) {
            if (!levels.isEmpty()) {
                q.append(WHERE).append("ul.level IN(");
                appendQuestionMarks(q, levels);
                q.append(AND).append(ACTIVE_PARAM);
            } else {
                q.append(WHERE).append(ACTIVE_PARAM);
            }
        }

        private void appendSearch(StringBuilder q, String key) {
            if (search != null && !search.isEmpty()) {
                String[] strings = search.split("[ ]+");
                q.append(key);
                for (String s : strings) {
                    q.append(NAME).append(ILIKE).append(String.format(SEARCH_PATTERN, s)).append(OR);
                }
                for (String s : strings) {
                    q.append(SURNAME).append(ILIKE).append(String.format(SEARCH_PATTERN, s)).append(OR);
                }
                q.replace(q.length() - 4, q.length(), "");
            }
        }
    }
}
