package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AppointmentItemDaoImpl extends AbstractDao<AppointmentItem> implements AppointmentItemDao {

    private static final Logger LOG = LogManager.getLogger(AppointmentItemDaoImpl.class);

    public AppointmentItemDaoImpl() {
        super(RowMapperFactory.getOrderingRowMapper(), Table.APPOINTMENT_ITEMS);
    }

    @Override
    public int create(AppointmentItem entity) throws DaoException {
        String query = INSERT + tableName + " (appointment_id, service_id)" + VALUES + "(?,?)";
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

    private void setItemStatement(AppointmentItem entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getAppointmentId());
        statement.setInt(++k, entity.getServiceId());
    }

    @Override
    public void update(AppointmentItem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws DaoException {
        return findAllByParam(appointmentId, Column.APPOINTMENT_ID);
    }
}
