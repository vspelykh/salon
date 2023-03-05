package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class provides an implementation of the BaseServiceDao interface using JDBC technology.
 * It extends the AbstractDao class and implements all the methods defined in BaseServiceDao.
 *
 * @version 1.0
 */
public class BaseServiceDaoImpl extends AbstractDao<BaseService> implements BaseServiceDao {

    private static final Logger LOG = LogManager.getLogger(BaseServiceDaoImpl.class);

    /**
     * Constructs a new instance of the BaseServiceDaoImpl class with the Base Service RowMapper and the BASE_SERVICE table.
     */
    public BaseServiceDaoImpl() {
        super(RowMapperFactory.getBaseServiceRowMapper(), Table.BASE_SERVICE);
    }

    /**
     * Returns a list of BaseServices filtered by categoriesIds, page and size.
     *
     * @param categoriesIds the list of category ids to filter by
     * @param page          the number of the page to return
     * @param size          the size of the page to return
     * @return a list of BaseServices filtered by categoriesIds, page and size
     * @throws DaoException if there is an issue executing the query
     */
    @Override
    public List<BaseService> findByFilter(List<Integer> categoriesIds, int page, int size) throws DaoException {
        BaseServiceFilteredQueryBuilder queryBuilder = new BaseServiceFilteredQueryBuilder(categoriesIds, page, size);
        String query = queryBuilder.buildQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<BaseService> baseServices = new ArrayList<>();
            while (resultSet.next()) {
                BaseService entity = rowMapper.map(resultSet);
                baseServices.add(entity);
            }
            return baseServices;
        } catch (SQLException e) {
            LOG.error(String.format("%s%s by filter params. Issue: %s", FAIL_FIND_LIST, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Returns the count of entities that belong to a list of categories.
     *
     * @param categoriesIds list of category IDs to filter by
     * @return the count of entities that belong to a list of categories
     * @throws DaoException if there is an issue with the data source or the query
     */
    @Override
    public int getCountOfCategories(List<Integer> categoriesIds) throws DaoException {
        int count;
        BaseServiceFilteredQueryBuilder queryBuilder = new BaseServiceFilteredQueryBuilder(categoriesIds);
        String query = queryBuilder.buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            } else {
                if (LOG.isEnabled(Level.ERROR)) {
                    LOG.error(String.format(LOG_FORMAT, FAIL_COUNT, tableName));
                }
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format("%s%s. Issue: %s", FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
        return count;
    }

    /**
     * Creates a new BaseService entity in the database.
     *
     * @param entity a BaseService object to be created.
     * @return an int value representing the ID of the newly created BaseService entity.
     * @throws DaoException if there is an issue with the SQL statement or execution.
     */
    @Override
    public int create(BaseService entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, CATEGORY_ID, SERVICE, SERVICE + UA, PRICE).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setBaseServiceStatement(entity, statement);
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

    /**
     * Updates the specified BaseService entity in the database.
     *
     * @param entity the BaseService entity to be updated.
     * @throws DaoException if there is an error updating the entity in the database.
     */
    @Override
    public void update(BaseService entity) throws DaoException {
        String query = new QueryBuilder().update(tableName).set(CATEGORY_ID, SERVICE, SERVICE + UA, PRICE).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            setBaseServiceStatement(entity, statement);
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Set parameters for the given PreparedStatement for creating or updating a BaseService entity
     *
     * @param entity    the BaseService entity containing values to be set
     * @param statement the PreparedStatement to set the values on
     * @throws SQLException if a database access error occurs
     */
    private void setBaseServiceStatement(BaseService entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getCategoryId());
        statement.setString(++k, entity.getService());
        statement.setString(++k, entity.getServiceUa());
        statement.setInt(++k, entity.getPrice());
        statement.setInt(++k, entity.getId());
    }

    /**
     * This class extends the QueryBuilder class and provides additional methods
     * for building queries with filtering by category IDs and pagination.
     *
     * @version 1.0
     */
    private class BaseServiceFilteredQueryBuilder extends QueryBuilder {

        private final List<Integer> categoriesIds;
        private Integer page;
        private Integer size;

        /**
         * Constructs a new BaseServiceFilteredQueryBuilder object with the given
         * category IDs, page number, and page size.
         *
         * @param categoriesIds The list of category IDs to filter by.
         * @param page          The page number for pagination.
         * @param size          The size of each page for pagination.
         */
        private BaseServiceFilteredQueryBuilder(List<Integer> categoriesIds, Integer page, Integer size) {
            this.categoriesIds = categoriesIds;
            this.page = page;
            this.size = size;
        }

        /**
         * Constructs a new BaseServiceFilteredQueryBuilder object with the given category IDs.
         * Uses for count query.
         *
         * @param categoriesIds The list of category IDs to filter by.
         */
        public BaseServiceFilteredQueryBuilder(List<Integer> categoriesIds) {
            this.categoriesIds = categoriesIds;
        }

        /**
         * Builds a query string with filtering by category IDs and pagination.
         *
         * @return The query string.
         */
        public String buildQuery() {
            select(tableName);
            appendCategoriesIds();
            return pagination(page, size).build();
        }

        /**
         * Builds a count query string with filtering by category IDs.
         *
         * @return The count query string.
         */
        public String buildCountQuery() {
            count(tableName);
            appendCategoriesIds();
            return build();
        }

        /**
         * Sets the parameters of a prepared statement with the given category IDs.
         *
         * @param preparedStatement The prepared statement to set parameters for.
         * @throws SQLException If an error occurs while setting parameters.
         */
        public void setParams(PreparedStatement preparedStatement) throws SQLException {
            int num = 0;
            if (!categoriesIds.isEmpty()) {
                for (Integer id : categoriesIds) {
                    preparedStatement.setInt(++num, id);
                }
            }
        }

        private void appendCategoriesIds() {
            if (!categoriesIds.isEmpty()) {
                whereIn(CATEGORY_ID, categoriesIds.size());
            }
        }
    }
}
