package ua.vspelykh.salon.dao.impl;

import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.OrderingDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class OrderingDaoImpl extends AbstractDao<Ordering> implements OrderingDao {

    public OrderingDaoImpl() {
        super(DBCPDataSource.getConnection(), RowMapperFactory.getOrderingRowMapper(), Table.ORDERING);
    }

    @Override
    public int create(Ordering entity) throws DaoException {
        String query = "INSERT INTO " + tableName + " (appointment_id, service_id)" + " VALUES " + "(?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setOrderingStatement(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException("No id for service was generated");
            }
        } catch (SQLException e) {
//            getLOG().error("Fail to insert item", e);
            throw new DaoException("Fail to insert service", e);
        }
    }

    private void setOrderingStatement(Ordering entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getAppointmentId());
        statement.setInt(++k, entity.getServiceId());
    }

    @Override
    public void update(Ordering entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Ordering> getByAppointmentId(Integer appointmentId) throws DaoException {
        return findAllByParam(appointmentId, Column.APPOINTMENT_ID);
    }
}
