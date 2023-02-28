package ua.vspelykh.salon.model.dao.impl;

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

public class BaseServiceDaoImpl extends AbstractDao<BaseService> implements BaseServiceDao {

    private static final Logger LOG = LogManager.getLogger(BaseServiceDaoImpl.class);

    public BaseServiceDaoImpl() {
        super(RowMapperFactory.getBaseServiceRowMapper(), Table.BASE_SERVICE);
    }

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
                LOG.error(FAIL_COUNT + tableName);
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format("%s%s. Issue: %s", FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
        return count;
    }

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
            LOG.error(String.format("%s%s. Issue: %s", FAIL_CREATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

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

    private void setBaseServiceStatement(BaseService entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getCategoryId());
        statement.setString(++k, entity.getService());
        statement.setString(++k, entity.getServiceUa());
        statement.setInt(++k, entity.getPrice());
        statement.setInt(++k, entity.getId());
    }

    private class BaseServiceFilteredQueryBuilder extends QueryBuilder {

        private final List<Integer> categoriesIds;
        private Integer page;
        private Integer size;

        private BaseServiceFilteredQueryBuilder(List<Integer> categoriesIds, Integer page, Integer size) {
            this.categoriesIds = categoriesIds;
            this.page = page;
            this.size = size;
        }

        public BaseServiceFilteredQueryBuilder(List<Integer> categoriesIds) {
            this.categoriesIds = categoriesIds;
        }

        public String buildQuery() {
            select(tableName);
            appendCategoriesIds();
            return pagination(page, size).build();
        }

        public String buildCountQuery() {
            count(tableName);
            appendCategoriesIds();
            return build();
        }

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