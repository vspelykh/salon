package ua.vspelykh.salon.dao.impl;

import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.AppointmentDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaoImpl extends AbstractDao<Appointment> implements AppointmentDao {

    public AppointmentDaoImpl() {
        super(DBCPDataSource.getConnection(), RowMapperFactory.getAppointmentRowMapper(), Table.APPOINTMENT);
    }

    @Override
    public List<Appointment> getByMasterId(Integer masterId) throws DaoException {
        return findAllByParam(masterId, Column.MASTER_ID);
    }

    @Override
    public List<Appointment> getByClientId(Integer clientId) throws DaoException {
        return findAllByParam(clientId, Column.CLIENT_ID);
    }

    @Override
    public List<Appointment> getByDateAndClientId(LocalDate date, int clientId) throws DaoException {
        return getByDateAndEntityId(date, clientId, Column.CLIENT_ID);
    }

    @Override
    public List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws DaoException {
        return getByDateAndEntityId(date, masterId, Column.MASTER_ID);
    }

    private List<Appointment> getByDateAndEntityId(LocalDate date, int entityId, String column) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(date, entityId, column);
        String query = queryBuilder.buildQuery();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int create(Appointment entity) throws DaoException {
        String query = INSERT + tableName + " (master_id, client_id, continuance, date, price, discount)"
                + VALUES + "(?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setAppointmentStatement(entity, statement);
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

    private int setAppointmentStatement(Appointment entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getMasterId());
        statement.setInt(++k, entity.getClientId());
        statement.setInt(++k, entity.getContinuance());
        statement.setTimestamp(++k, Timestamp.valueOf(entity.getDate()));
        statement.setInt(++k, entity.getPrice());
        statement.setInt(++k, entity.getDiscount());
        return ++k;
    }

    @Override
    public void update(Appointment entity) throws DaoException {
        String query = "UPDATE appointments SET master_id = ?, client_id = ?, continuance = ?, date = ?" +
                ", price = ?, discount = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(setAppointmentStatement(entity, statement), entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private class AppointmentQueryBuilder {

        private final LocalDate date;
        private final int entityId;
        private final String columnName;

        public AppointmentQueryBuilder(LocalDate date, int entityId, String columnName) {
            this.date = date;
            this.entityId = entityId;
            this.columnName = columnName;
        }

        public String buildQuery() {
            return SELECT + tableName + " WHERE " + "DATE(date) = ? AND " + columnName + "=?";
        }

        public void setParams(PreparedStatement preparedStatement) throws SQLException {
            int k = 0;
            preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
            preparedStatement.setInt(++k, entityId);
        }
    }
}

