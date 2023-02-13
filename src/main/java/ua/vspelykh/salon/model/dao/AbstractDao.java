package ua.vspelykh.salon.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.Table.USER_ROLES;

public abstract class AbstractDao<T> implements Dao<T> {

    private static final Logger LOG = LogManager.getLogger();

    protected static final String FAIL_UPDATE = "Fail to update item in";
    protected static final String FAIL_CREATE = "Fail to create item in";
    protected static final String FAIL_FIND = "Fail to find entity ";
    protected static final String FAIL_FIND_LIST = "Fail to find entities ";
    protected static final String FAIL_DELETE = "Fail to delete";
    protected static final String NO_ID = "No id was generated in ";
    protected static final String ACTIVE_PARAM = "ul.active='true'";

    protected static final String SELECT = "SELECT * FROM ";
    protected static final String INSERT = "INSERT INTO ";
    protected static final String VALUES = " VALUES ";
    protected static final String DELETE = "DELETE FROM ";
    protected static final String WHERE = " WHERE ";
    protected static final String EQUAL = "=?";
    protected static final String NOT_EQUAL = "!=?";
    protected static final String INNER_JOIN = " INNER JOIN ";
    protected static final String LIMIT = " LIMIT ";
    protected static final String OFFSET = " OFFSET ";
    protected static final String ORDER_BY = " ORDER BY ";
    protected static final String NAME_ASC = "name asc";
    protected static final String NAME_DESC = "name desc";
    protected static final String LEVEL_EXP = "level asc";
    protected static final String LEVEL_YOUNG = "level desc";
    protected static final String ILIKE = " ILIKE ";
    protected static final String OR = " OR ";
    protected static final String AND = " AND ";
    protected static final String ON_CONFLICT = "  ON CONFLICT ";
    protected static final String DO_UPDATE = "  DO UPDATE ";
    protected static final String SET = "SET ";
    protected static final String HAVING = " HAVING ";
    protected static final String SEARCH_PATTERN = "'%%%s%%'";

    protected static final String SELECT_USERS = "SELECT u.id, name, surname, email, number, password, AVG(coalesce(mark,0)) " +
            "as average FROM users u INNER JOIN user_level ul ON u.id = ul.id LEFT JOIN feedbacks m ON u.id=" +
            "(SELECT master_id FROM appointments a WHERE m.appointment_id=a.id)";

    protected static final String COUNT_MASTERS_QUERY = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id ";
    protected static final String COUNT_SERVICES_QUERY = "SELECT COUNT(1) FROM base_services";
    protected static final String ADD_ROLE_QUERY = INSERT + USER_ROLES + " VALUES (?,?)";
    protected static final String UPDATE_ROLE_QUERY = DELETE + USER_ROLES + WHERE + Column.USER_ID + EQUAL + AND + Column.ROLE + EQUAL;

    private Connection connection;
    protected RowMapper<T> rowMapper;
    protected final String tableName;

    protected AbstractDao(RowMapper<T> rowMapper, String tableName) {
        this.rowMapper = rowMapper;
        this.tableName = tableName;
    }

    @Override
    public T findById(int id) throws DaoException {
        return findByParam(id, Column.ID);
    }

    @Override
    public List<T> findAll() throws DaoException {
        String query = SELECT + tableName;
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<T> entities = new ArrayList<>();
            while (resultSet.next()) {
                T entity = rowMapper.map(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public void removeById(int id) throws DaoException {
        String query = DELETE + tableName + WHERE + Column.ID + EQUAL;
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            int i = statement.executeUpdate();
            if (i != 1) {
                LOG.error("Item for delete didn't find in {}", tableName);
                throw new DaoException("No item was deleted");
            }
        } catch (SQLException e) {
            LOG.error("Fail to delete item.", e);
            throw new DaoException(e);
        }
    }

    protected T findByParam(Object value, String param) throws DaoException {
        T entity;
        String query = SELECT + tableName + WHERE + param + EQUAL;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entity = rowMapper.map(resultSet);
            } else {
                LOG.error("No entity from {} found by {} with value {}.", tableName, param, value);
                throw new DaoException(String.format("No entity from %s found by %s with value %s", tableName, param, value));
            }
        } catch (SQLException e) {
            LOG.error(String.format("%sin%s by %s", FAIL_FIND, tableName, param));
            throw new DaoException(e);
        }
        return entity;
    }

    protected List<T> findAllByParam(Object value, String param) throws DaoException {
        List<T> entities = new ArrayList<>();
        String query = SELECT + tableName + WHERE + param + EQUAL;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                entities.add(rowMapper.map(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            LOG.error(String.format("%s%s by %s with value %s", FAIL_FIND_LIST, tableName, param, value), e);
            throw new DaoException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
