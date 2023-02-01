package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.AppointmentDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class AppointmentDaoImpl extends AbstractDao<Appointment> implements AppointmentDao {

    private static final Logger LOG = LogManager.getLogger(AppointmentDaoImpl.class);

    public AppointmentDaoImpl() {
        super(RowMapperFactory.getAppointmentRowMapper(), Table.APPOINTMENT);
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
    public List<Appointment> getAllByDate(LocalDate date) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(date);
        String query = queryBuilder.buildAppointmentsForEmailQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParamsForEmailQuery(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<Appointment> getFiltered(Integer masterId, LocalDate dateFrom, LocalDate dateTo,
                                         AppointmentStatus status, int page, int size) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(masterId, dateFrom, dateTo, status, page, size);
        String query = queryBuilder.buildFilteredQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setFilteredParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public int getCountOfAppointments(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status) throws DaoException {
        int count;
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(masterId, dateFrom, dateTo, status);
        String query = queryBuilder.buildCountQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setFilteredParams(preparedStatement);
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
    public List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws DaoException {
        return getByDateAndEntityId(date, masterId, Column.MASTER_ID);
    }

    private List<Appointment> getByDateAndEntityId(LocalDate date, int entityId, String column) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(date, entityId, column);
        String query = queryBuilder.buildQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public int create(Appointment entity) throws DaoException {
        String query = INSERT + tableName + " (master_id, client_id, continuance, date, price, discount)"
                + VALUES + "(?,?,?,?,?,?)";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setAppointmentStatement(entity, statement);
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
    public void update(Appointment entity) throws DaoException {
        String query = "UPDATE appointments SET master_id = ?, client_id = ?, continuance = ?, date = ?" +
                ", price = ?, discount = ?, status = ? WHERE id = ?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(setAppointmentStatement(entity, statement), entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(FAIL_UPDATE, e);
            throw new DaoException(FAIL_UPDATE + tableName, e);
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
        statement.setString(++k, entity.getStatus().name());
        return ++k;
    }

    private class AppointmentQueryBuilder {

        private final String dateQuery = SELECT + tableName + WHERE + "DATE(date)" + EQUAL;
        private LocalDate date;
        private Integer entityId;
        private String columnName;

        private LocalDate dateFrom;
        private LocalDate dateTo;
        private AppointmentStatus status;
        private int page;
        private int size;

        public AppointmentQueryBuilder(LocalDate date, int entityId, String columnName) {
            this.date = date;
            this.entityId = entityId;
            this.columnName = columnName;
        }

        public AppointmentQueryBuilder(LocalDate date) {
            this.date = date;
        }

        public AppointmentQueryBuilder(Integer masterId, LocalDate dateFrom, LocalDate dateTo,
                                       AppointmentStatus status, int page, int size) {
            this.entityId = masterId;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.status = status;
            this.page = page;
            this.size = size;
        }

        public AppointmentQueryBuilder(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status) {
            this.entityId = masterId;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.status = status;
        }

        public String buildQuery() {
            return dateQuery + AND + columnName + EQUAL + AND + STATUS + NOT_EQUAL + ORDER_BY + DATE;
        }

        public void setParams(PreparedStatement preparedStatement) throws SQLException {
            int k = 0;
            preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
            preparedStatement.setInt(++k, entityId);
            preparedStatement.setString(++k, AppointmentStatus.CANCELLED.name());
        }

        public void setFilteredParams(PreparedStatement preparedStatement) throws SQLException {
            int k = 0;
            if (entityId != null) {
                preparedStatement.setInt(++k, entityId);
            }
            if (status != null) {
                preparedStatement.setString(++k, status.name());
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(dateFrom, LocalTime.MIN)));
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(dateTo, LocalTime.MIN)));
            }
        }

        public String buildAppointmentsForEmailQuery() {
            return dateQuery;
        }

        public void setParamsForEmailQuery(PreparedStatement preparedStatement) throws SQLException {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
        }

        public String buildFilteredQuery() {
            StringBuilder query = new StringBuilder(SELECT).append(tableName);
            appendingFilteredParams(query);
            return appendPagingAndOrderParams(query);
        }

        public String buildCountQuery() {
            StringBuilder query = new StringBuilder("SELECT COUNT(1) FROM ");
            query.append(tableName);
            return appendingFilteredParams(query);
        }

        private String appendingFilteredParams(StringBuilder query) {
            if (isOneFilteredParamNotNull()){
                query.append(WHERE);
            }
            int count = 0;
            count = appendMasterId(query, count);
            count = appendStatus(query, count);
            appendDates(query, count);
            return query.toString();
        }

        private boolean isOneFilteredParamNotNull() {
            return entityId != null || dateFrom != null || dateTo != null || status != null;
        }

        private String appendPagingAndOrderParams(StringBuilder query) {
            query.append(ORDER_BY).append(DATE).append(" DESC");
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


        private int appendMasterId(StringBuilder query, int count) {
            if (entityId != null) {
                query.append(MASTER_ID).append(EQUAL);
                count++;
            }
            return count;
        }

        private int appendStatus(StringBuilder query, int count) {
            if (status != null) {
                appendAnd(query, count);
                count++;
                query.append(STATUS).append(EQUAL);
            }
            return count;
        }


        private void appendDates(StringBuilder query, int count) {
            if (dateFrom != null && dateTo != null) {
                appendAnd(query, count);
                query.append("DATE(date) >= ?").append(AND).append("DATE(date) <= ?");
            } else if (dateFrom != null) {
                query.append("DATE(date) >= ?");
            } else if (dateTo != null){
                appendAnd(query, count);
                query.append("DATE(date) <= ?");
            }
        }
        private void appendAnd(StringBuilder q, int count){
            if (count > 0){
                q.append(AND);
            }
        }
    }
}

