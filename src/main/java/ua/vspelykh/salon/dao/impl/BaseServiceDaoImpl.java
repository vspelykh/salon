package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.BaseServiceDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.dao.mapper.Column.CATEGORY_ID;

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
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws DaoException {
        int count;
        BaseServiceFilteredQueryBuilder queryBuilder = new BaseServiceFilteredQueryBuilder(categoriesIds, page, size);
        String query = queryBuilder.buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
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

    @Override
    public int create(BaseService entity) throws DaoException {
        String query = INSERT + tableName + " (service, service_ua, price)" + VALUES + "(?,?,?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(statement, entity);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException(NO_ID + tableName);
            }
        } catch (SQLException e) {
            LOG.error(FAIL_CREATE + tableName, e);
            throw new DaoException(FAIL_CREATE + tableName, e);
        }
    }

    private void setStatement(PreparedStatement statement, BaseService entity) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getService());
        statement.setString(++k, entity.getServiceUa());
        statement.setInt(++k, entity.getPrice());
    }

    @Override
    public void update(BaseService entity) throws DaoException {
        String query = "UPDATE base_services SET service = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, entity.getService());
            statement.setInt(2, entity.getPrice());
            statement.setInt(3, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(FAIL_UPDATE, e);
            throw new DaoException(FAIL_UPDATE + tableName, e);
        }
    }

    private class BaseServiceFilteredQueryBuilder {

        private List<Integer> categoriesIds;
        private final Integer page;
        private final Integer size;

        private BaseServiceFilteredQueryBuilder(List<Integer> categoriesIds, Integer page, Integer size) {
            this.categoriesIds = categoriesIds;
            this.page = page;
            this.size = size;
        }

        private String buildQuery() {
            StringBuilder query = new StringBuilder(SELECT + tableName);
            appendCategoriesIds(query);
            return setPagingParamsAndGetQuery(query);
        }

        private String setPagingParamsAndGetQuery(StringBuilder query) {
            int offset;
            if (page == 1) {
                offset = 0;
            } else {
                offset = (page - 1) * size;
            }
            query.append(LIMIT).append(size);
            query.append(OFFSET).append(offset);
            return query.toString();
        }

        private void appendQuestionMarks(StringBuilder query, List<?> userIds) {
            for (int i = 0; i < userIds.size(); i++) {
                query.append("?");
                if (i != userIds.size() - 1)
                    query.append(",");
            }
            query.append(")");
        }

        private void setParams(PreparedStatement preparedStatement) throws SQLException {
            int num = 0;
            if (!categoriesIds.isEmpty()) {
                for (Integer id : categoriesIds) {
                    preparedStatement.setInt(++num, id);
                }
            }
        }

        public String buildCountQuery() {
            StringBuilder query = new StringBuilder(COUNT_SERVICES_QUERY);
            appendCategoriesIds(query);
            return query.toString();
        }

        private void appendCategoriesIds(StringBuilder query) {
            if (!categoriesIds.isEmpty()) {
                query.append(WHERE + CATEGORY_ID + " IN(");
                appendQuestionMarks(query, categoriesIds);
            }
        }
    }
}
