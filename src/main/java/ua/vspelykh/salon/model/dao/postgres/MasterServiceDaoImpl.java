package ua.vspelykh.salon.model.dao.postgres;

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

/**
 * This class represents the implementation of the MasterServiceDao interface.
 * It extends the AbstractDao class and provides implementation for all the methods declared in MasterServiceDao.
 *
 * @version 1.0
 */
public class MasterServiceDaoImpl extends AbstractDao<MasterService> implements MasterServiceDao {

    private static final Logger LOG = LogManager.getLogger(MasterServiceDaoImpl.class);

    /**
     * Constructor of MasterServiceDaoImpl class. It calls the super constructor with arguments for creating
     * a new instance of MasterServiceDaoImpl.
     */
    public MasterServiceDaoImpl() {
        super(RowMapperFactory.getMasterServiceRowMapper(), Table.MASTER_SERVICES);
    }

    /**
     * Creates a new master service in the database.
     *
     * @param entity an object of MasterService that is to be created in the database.
     * @return an int value representing the id of the newly created MasterService object.
     * @throws DaoException if there is an error while performing the create operation.
     */
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

    /**
     * Sets the parameters of the PreparedStatement object for creating a new MasterService object.
     *
     * @param entity    an object of MasterService that is to be created in the database.
     * @param statement a PreparedStatement object used for executing a SQL statement that inserts a new MasterService object into the database.
     * @throws SQLException if there is an error while setting the parameters of the PreparedStatement object.
     */
    private void setServiceStatement(MasterService entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getMasterId());
        statement.setInt(++k, entity.getBaseServiceId());
        statement.setInt(++k, entity.getContinuance());
    }

    /**
     * Updates an existing MasterService object in the database.
     *
     * @param entity an object of MasterService that is to be updated in the database.
     * @throws DaoException if there is an error while performing the update operation.
     */
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

    /**
     * Finds all MasterService objects associated with a particular user id in the database.
     *
     * @param userId an integer representing the user id for which to retrieve MasterService objects.
     * @return a list of MasterService objects associated with the given user id.
     * @throws DaoException if there is an error while performing the find operation.
     */
    @Override
    public List<MasterService> getAllByUserId(Integer userId) throws DaoException {
        return findAllByParam(userId, MASTER_ID);
    }

    /**
     * Finds all MasterService objects associated with a particular BaseService id in the database.
     *
     * @param baseServiceId an integer representing the BaseService id for which to retrieve MasterService objects.
     * @return a list of MasterService objects associated with the given user id.
     * @throws DaoException if there is an error while performing the find operation.
     */
    @Override
    public List<MasterService> getAllByBaseServiceId(Integer baseServiceId) throws DaoException {
        return findAllByParam(baseServiceId, Column.BASE_SERVICE_ID);
    }
}
