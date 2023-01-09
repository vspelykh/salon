package ua.vspelykh.salon.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.dao.Table.USER_LEVEL;
import static ua.vspelykh.salon.dao.Table.USER_ROLES;
import static ua.vspelykh.salon.dao.mapper.Column.ROLE;
import static ua.vspelykh.salon.dao.mapper.Column.USER_ID;

public abstract class AbstractDao<T> implements Dao<T> {

    private static final Logger LOG = LogManager.getLogger();

    protected static final String FAIL_UPDATE = "Fail to update item in";
    protected static final String FAIL_CREATE = "Fail to create item in";
    protected static final String FAIL_FIND = "Fail to find entity ";
    protected static final String FAIL_FIND_LIST = "Fail to find entities ";
    protected static final String NO_ID = "No id was generated in ";

    protected static final String SELECT = "SELECT * FROM ";
    protected static final String INSERT = "INSERT INTO ";
    protected static final String VALUES = " VALUES ";
    protected static final String DELETE = "DELETE FROM ";
    protected static final String WHERE = " WHERE ";
    protected static final String EQUAL = "=?";
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

    protected static final String COUNT_MASTERS_QUERY = "SELECT COUNT(1) FROM users u INNER JOIN user_level ul ON u.id = ul.id ";
    protected static final String ADD_ROLE_QUERY = INSERT + USER_ROLES + " VALUES (?,?)";
    protected static final String UPDATE_ROLE_QUERY = DELETE + USER_ROLES + WHERE + USER_ID + EQUAL + AND + ROLE + EQUAL;

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
        try (Connection connection = DBCPDataSource.getConnection();
             Statement statement = connection.createStatement();
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
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int i = statement.executeUpdate();
            if (i != 1) {
                LOG.error("Item for delete didn't find in {}", tableName);
                throw new DaoException("No item was deleted");
            }
        } catch (SQLException e) {
            LOG.error("Fail to delete item.", e);
            throw new DaoException("Fail to delete item, e");
        }
    }

    protected T findByParam(Object value, String param) throws DaoException {
        T entity;
        String query = SELECT + tableName + WHERE + param + EQUAL;
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entity = rowMapper.map(resultSet);
            } else {
                LOG.error("No entity from {} found by {} with value {}.", tableName, param, value);
                throw new DaoException("No entity from " + tableName + " found by " + param + " with value " + value);
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException("Fail to find entity in " + tableName + " by " + param + " with value " + value, e);
        }
        return entity;
    }

    protected List<T> findAllByParam(Object value, String param) throws DaoException {
        List<T> entities = new ArrayList<>();
        String query = SELECT + tableName + WHERE + param + EQUAL;
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entities.add(rowMapper.map(resultSet));
            } else {
                LOG.error("No entities from {} found by {} with value {}.", tableName, param, value);
                throw new DaoException("No entities from " + tableName + " found by " + param + " with value " + value);
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(FAIL_FIND_LIST + tableName + " by " + param + " with value " + value, e);
        }
        return entities;
    }
}
