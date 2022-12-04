package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.BaseServiceDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseServiceDaoImpl extends AbstractDao<BaseService> implements BaseServiceDao {

    private static final Logger LOG = LogManager.getLogger(BaseServiceDaoImpl.class);

    public BaseServiceDaoImpl() {
        super(DBCPDataSource.getConnection(), RowMapperFactory.getBaseServiceRowMapper(), Table.BASE_SERVICE);
    }

    @Override
    public List<BaseService> findByFilter(String name, Integer priceFrom, Integer priceTo) throws DaoException {
        BaseServiceFilteredQueryBuilder queryBuilder = new BaseServiceFilteredQueryBuilder(name, priceFrom, priceTo);
        String query = queryBuilder.buildQuery();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
    public int create(BaseService entity) throws DaoException {
        String query = INSERT + tableName + " (service, price)" + VALUES + "(?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getService());
            statement.setInt(2, entity.getPrice());
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

    @Override
    public void update(BaseService entity) throws DaoException {
        String query = "UPDATE base_services SET service = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
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

        private String name;
        private final Integer priceFrom;
        private final Integer priceTo;

        private BaseServiceFilteredQueryBuilder(String name, Integer priceFrom, Integer priceTo) {
            this.name = name;
            if (priceFrom == null) {
                this.priceFrom = 0;
            } else {
                this.priceFrom = priceFrom;
            }
            this.priceTo = priceTo;
        }

        private String buildQuery() {
            StringBuilder query = new StringBuilder(SELECT + tableName + " WHERE service LIKE ?");
            if (name == null || name.isEmpty()) {
                name = "%";
            } else name = "%" + name + "%";
            query.append(" AND price>=?");
            if (priceTo != null) {
                query.append(" AND price<=?");
            }
            return query.toString();
        }

        private void setParams(PreparedStatement preparedStatement) {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, priceFrom);
                if (priceTo != null) {
                    preparedStatement.setInt(3, priceTo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
