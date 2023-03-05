package ua.vspelykh.salon.model.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractDao is an abstract class that implements the interface Dao. It provides basic functionality
 * for data access objects.
 *
 * @param <T> type of entity to be persisted
 * @version 1.0
 */
public abstract class AbstractDao<T> implements Dao<T> {

    private static final Logger LOG = LogManager.getLogger();

    /**
     * Constants for logging errors
     */
    protected static final String LOG_PATTERN = "%s%s. Issue: %s";
    protected static final String FAIL_UPDATE = "Fail to update item in ";
    protected static final String FAIL_CREATE = "Fail to create item in ";
    protected static final String FAIL_FIND = "Fail to find entity ";
    protected static final String FAIL_COUNT = "Fail to count items in ";
    protected static final String FAIL_FIND_LIST = "Fail to find entities ";
    protected static final String FAIL_DELETE = "Fail to delete";
    protected static final String NO_ID = "No id was generated in ";
    protected static final String LOG_FORMAT = "%s%s";

    private Connection connection;
    protected RowMapper<T> rowMapper;
    protected final String tableName;

    /**
     * Constructs a new Dao instance with the given row mapper and table name.
     *
     * @param rowMapper row mapper used to map ResultSet rows to entities of type T
     * @param tableName name of the table in the database associated with the entity type T
     */
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
        String query = new QueryBuilder().select(tableName).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
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
        String query = new QueryBuilder().delete(tableName).where(Column.ID).build();
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

    /**
     * Find a single entity by the given parameter value.
     *
     * @param value the parameter value to search for
     * @param param the name of the column to search in
     * @return the entity matching the specified parameter value
     * @throws DaoException if there is an error executing the query
     */
    protected T findByParam(Object value, String param) throws DaoException {
        T entity;
        String query = new QueryBuilder().select(tableName).where(param).build();
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

    /**
     * Finds all entities that match the specified parameter and value.
     *
     * @param value the value of the parameter
     * @param param the parameter to search by
     * @return a list of entities that match the parameter and value
     * @throws DaoException if there is an error executing the query
     */
    protected List<T> findAllByParam(Object value, String param) throws DaoException {
        List<T> entities = new ArrayList<>();
        String query = new QueryBuilder().select(tableName).where(param).build();
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

    /**
     * Returns the current connection to the database.
     *
     * @return the current connection
     */
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
