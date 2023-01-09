package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ua.vspelykh.salon.dao.Table.USER_LEVEL;
import static ua.vspelykh.salon.dao.mapper.Column.NAME;
import static ua.vspelykh.salon.dao.mapper.Column.SURNAME;
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
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
            LOG.error(String.format("%s %s", FAIL_CREATE, tableName), e);
            throw new DaoException(FAIL_CREATE + tableName, e);
        }
    }

    @Override
    public void update(User entity) throws DaoException {
        String query = "UPDATE users SET name = ?, surname = ?, email = ?, number = ?, password = ? WHERE id = ?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            encryptPassword(entity);
            setUserStatement(entity, statement);
            statement.setInt(6, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(FAIL_UPDATE, e);
            throw new DaoException(FAIL_UPDATE + tableName, e);
        }
    }

    private void encryptPassword(User user) throws DaoException {
        if (user.isNew() || !findById(user.getId()).getPassword().equals(user.getPassword())) {
            BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
            checkPassword(user.getPassword());
            user.setPassword(encryptor.encryptPassword(user.getPassword()));
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws DaoException {
        User user;
        String query = SELECT + tableName + " WHERE email=? AND password=?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
            throw new DaoException(FAIL_FIND + tableName + " by email and password");
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
    public List<User> findMastersByLevelsAndServices(List<MastersLevel> levels, List<Integer> serviceIds,
                                                     String search, int page, int size, MasterSort sort) throws DaoException {
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(levels, serviceIds, page, size, sort, search);
        String query = queryBuilder.buildQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> masters = new ArrayList<>();
            while (resultSet.next()) {
                User entity = rowMapper.map(resultSet);
                masters.add(entity);
            }
            return masters;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAdministrators() throws DaoException {
        return findByRole(Role.ADMINISTRATOR);
    }

    @Override
    public int getCountOfMasters(List<MastersLevel> levels, List<Integer> serviceIds, String search) throws DaoException {
        int count;
        MasterFilteredQueryBuilder queryBuilder = new MasterFilteredQueryBuilder(levels, serviceIds, search);
        String query = queryBuilder.buildCountQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            } else {
                //TODO
                throw new DaoException("TODO");
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("TODO");
        }
        return count;
    }

    private List<User> findByRole(Role role) throws DaoException {
        String query = SELECT + "users u INNER JOIN user_roles ur ON u.id = ur.user_id AND ur.role=?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
            LOG.error(e);
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
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<Role> roles = user.getRoles();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString(Column.ROLE)));
            }
            return user;
        } catch (SQLException e) {
            LOG.error("Error during setting roles for user");
            throw new DaoException("Error during setting roles for user", e);
        }
    }

    private class MasterFilteredQueryBuilder {

        private List<MastersLevel> levels;
        private List<Integer> serviceIds;
        private int page;
        private int size;
        private MasterSort sort;
        private String search;

        public MasterFilteredQueryBuilder(List<MastersLevel> levels, List<Integer> serviceIds,
                                          int page, int size, MasterSort sort, String search) {
            this.levels = levels;
            this.serviceIds = serviceIds;
            this.page = page;
            this.size = size;
            this.sort = sort;
            this.search = search;
        }

        public MasterFilteredQueryBuilder(List<MastersLevel> levels, List<Integer> serviceIds, String search) {
            this.levels = levels;
            this.serviceIds = serviceIds;
            this.search = search;
        }

        private String buildQuery() {
            StringBuilder query = new StringBuilder(SELECT + tableName).append(" u");
            if (isAllListsAndSearchAreEmpty()) {
                return shortQuery();
            } else {
                query.append(INNER_JOIN).append(USER_LEVEL).append(" ").append("ul ");
                query.append("ON u.id=ul.user_id ");
                appendLevels(query);
                appendServices(query);
                appendSearch(query);
            }
            return addPagingParams(query);
        }

        private void appendSearch(StringBuilder query) {
            if (search != null && !search.isEmpty()) {
                String[] strings = search.split("[ ]+");
                query.append(WHERE);
                for (String s : strings) {
                    query.append(NAME).append(ILIKE).append(String.format("'%%%s%%'", s)).append(OR);
                }
                for (String s : strings) {
                    query.append(SURNAME).append(ILIKE).append(String.format("'%%%s%%'", s)).append(OR);
                }
                query.replace(query.length() - 4, query.length(), "");
            }
        }

        private void appendServices(StringBuilder query) {
            if (!serviceIds.isEmpty()) {
                query.append(INNER_JOIN).append("(SELECT master_id from services s").append(WHERE);
                query.append("s.base_service_id IN(");
                appendQuestionMarks(query, serviceIds);
                query.append(" GROUP BY s.master_id) AS q ON q.master_id = u.id");
            }
        }

        private void appendLevels(StringBuilder query) {
            if (!levels.isEmpty()) {
                query.append("and ul.level IN(");
                appendQuestionMarks(query, levels);
            }
        }

        private String buildCountQuery() {
            StringBuilder query = new StringBuilder(COUNT_MASTERS_QUERY);
            if (isAllListsAndSearchAreEmpty()) {
                return query.toString();
            }
            appendLevels(query);
            appendServices(query);
            appendSearch(query);
            System.out.println(query);
            return query.toString();
        }

        private String shortQuery() {
            StringBuilder q = new StringBuilder(SELECT + tableName + " u" + INNER_JOIN + "user_level ul ON u.id = ul.user_id");
            return addPagingParams(q);
        }

        private String addPagingParams(StringBuilder q) {
            addSortingParams(q);
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
            switch (sort) {
                case NAME_ASC:
                    q.append(NAME_ASC);
                    break;
                case NAME_DESC:
                    q.append(NAME_DESC);
                    break;
                case FIRST_PRO:
                    q.append(LEVEL_EXP);
                    break;
                case FIRST_YOUNG:
                    q.append(LEVEL_YOUNG);
                    break;
                case RATING_ASC:
                    //TODO
                    break;
                case RATING_DESC:
                    //TODO
                    break;
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
            return levels.isEmpty() && serviceIds.isEmpty() && (search == null || search.isEmpty());
        }

        private void setParams(PreparedStatement preparedStatement) {
            try {
                int paramNum = 1;
                if (!levels.isEmpty()) {
                    for (MastersLevel level : levels) {
                        preparedStatement.setString(paramNum++, level.name());
                    }
                }
                if (!serviceIds.isEmpty()) {
                    for (Integer serviceId : serviceIds) {
                        preparedStatement.setInt(paramNum++, serviceId);
                    }
                }
            } catch (SQLException e) {
//                TODO
                e.printStackTrace();
            }
        }
    }
}
