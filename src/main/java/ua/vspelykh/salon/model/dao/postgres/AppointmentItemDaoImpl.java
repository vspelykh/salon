package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.impl.AppointmentItemRowMapper;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.APPOINTMENT_ID;
import static ua.vspelykh.salon.model.dao.mapper.Column.SERVICE_ID;

/**
 * AppointmentItemDaoImpl class that extends AbstractDao and implements AppointmentItemDao interface.
 * Handles CRUD operations for AppointmentItem objects in the database.
 *
 * @version 1.0
 */
public class AppointmentItemDaoImpl extends AbstractDao<AppointmentItem> implements AppointmentItemDao {

    private static final Logger LOG = LogManager.getLogger(AppointmentItemDaoImpl.class);

    /**
     * Constructor for the AppointmentItemDaoImpl class.
     */
    public AppointmentItemDaoImpl() {
        super(new AppointmentItemRowMapper(), Table.APPOINTMENT_ITEMS);
    }

    /**
     * Creates a new AppointmentItem object in the database.
     *
     * @param entity AppointmentItem object to be created
     * @return the generated ID of the created AppointmentItem object
     * @throws DaoException if an error occurs while executing the SQL query
     */
    @Override
    public int create(AppointmentItem entity) throws DaoException {

        String query = new QueryBuilder().insert(tableName, APPOINTMENT_ID, SERVICE_ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setItemStatement(entity, statement);
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
     * Sets the values of a PreparedStatement object for inserting a new AppointmentItem object into the database.
     *
     * @param entity    AppointmentItem object to be inserted
     * @param statement PreparedStatement object to be executed
     * @throws SQLException if an error occurs while setting the values of the PreparedStatement
     */
    private void setItemStatement(AppointmentItem entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getAppointmentId());
        statement.setInt(++k, entity.getServiceId());
    }

    /***
     * @throws UnsupportedOperationException always, as updating AppointmentItems is not supported
     */
    @Override
    public void update(AppointmentItem entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves a list of AppointmentItem objects from the database with the given appointment ID.
     *
     * @param appointmentId ID of the Appointment object that the AppointmentItem objects are associated with
     * @return a list of AppointmentItem objects with the given appointment ID
     * @throws DaoException if an error occurs while executing the SQL query
     */
    @Override
    public List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws DaoException {
        return findAllByParam(appointmentId, APPOINTMENT_ID);
    }
}
