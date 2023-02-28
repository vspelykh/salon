package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.MasterServiceDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

public class MasterServiceDaoImpl extends AbstractDao<MasterService> implements MasterServiceDao {

    private static final Logger LOG = LogManager.getLogger(MasterServiceDaoImpl.class);

    public MasterServiceDaoImpl() {
        super(RowMapperFactory.getMasterServiceRowMapper(), Table.MASTER_SERVICES);
    }

    @Override
    public int create(MasterService entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, MASTER_ID, BASE_SERVICE_ID, CONTINUANCE).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setServiceStatement(entity, statement);
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

    private void setServiceStatement(MasterService entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getMasterId());
        statement.setInt(++k, entity.getBaseServiceId());
        statement.setInt(++k, entity.getContinuance());
    }

    @Override
    public void update(MasterService entity) throws DaoException {
        String query = new QueryBuilder().update(tableName).set(MASTER_ID, BASE_SERVICE_ID, CONTINUANCE).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(4, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public List<MasterService> getAllByUserId(Integer userId) throws DaoException {
        return findAllByParam(userId, MASTER_ID);
    }

    @Override
    public List<MasterService> getAllByBaseServiceId(Integer baseServiceId) throws DaoException {
        return findAllByParam(baseServiceId, Column.BASE_SERVICE_ID);
    }
}
