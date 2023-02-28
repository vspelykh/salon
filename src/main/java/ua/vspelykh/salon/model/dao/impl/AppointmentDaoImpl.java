package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

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
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParamsForEmailQuery(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            LOG.error(String.format("%sby date in %s. Issue: %s", FAIL_FIND, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public List<Appointment> getFiltered(Integer masterId, LocalDate dateFrom, LocalDate dateTo,
                                         AppointmentStatus status, PaymentStatus paymentStatus, int page, int size) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(masterId, dateFrom, dateTo, status, paymentStatus, page, size);
        String query = queryBuilder.buildFilteredQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setFilteredParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            LOG.error(String.format("%sby filter params in %s. Issue: %s", FAIL_FIND, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public int getCountOfAppointments(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus) throws DaoException {
        int count;
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(masterId, dateFrom, dateTo, status, paymentStatus);
        String query = queryBuilder.buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setFilteredParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            } else {
                LOG.error(FAIL_COUNT + tableName);
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
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
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (resultSet.next()) {
                Appointment entity = rowMapper.map(resultSet);
                appointments.add(entity);
            }
            return appointments;
        } catch (SQLException e) {
            LOG.error(String.format("%s in %s by date and %s. Issue: %s", FAIL_FIND, tableName, column, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public int create(Appointment entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, MASTER_ID, CLIENT_ID, CONTINUANCE, DATE,
                PRICE, DISCOUNT, STATUS, PAYMENT_STATUS).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setAppointmentStatement(entity, statement);
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

    @Override
    public void update(Appointment entity) throws DaoException {
        String query = new QueryBuilder().update(tableName).set(MASTER_ID, CLIENT_ID, CONTINUANCE, DATE, PRICE,
                DISCOUNT, STATUS, PAYMENT_STATUS).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(setAppointmentStatement(entity, statement), entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
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
        statement.setString(++k, entity.getPaymentStatus().name());
        return ++k;
    }

    private class AppointmentQueryBuilder extends QueryBuilder {

        private final String dateQuery = new QueryBuilder().select(tableName).where("DATE(date)").build();
        private LocalDate date;
        private Integer entityId;
        private String columnName;

        private LocalDate dateFrom;
        private LocalDate dateTo;
        private AppointmentStatus status;
        private PaymentStatus paymentStatus;
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
                                       AppointmentStatus status, PaymentStatus paymentStatus, int page, int size) {
            this.entityId = masterId;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.status = status;
            this.paymentStatus = paymentStatus;
            this.page = page;
            this.size = size;
        }

        public AppointmentQueryBuilder(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status,
                                       PaymentStatus paymentStatus) {
            this.entityId = masterId;
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.status = status;
            this.paymentStatus = paymentStatus;
        }

        public String buildQuery() {
            return append(dateQuery).and(columnName).andNotEqual(STATUS).orderBy(DATE).build();
        }

        public String buildAppointmentsForEmailQuery() {
            return dateQuery;
        }

        public String buildFilteredQuery() {
            select(tableName);
            appendingFilteredParams();
            appendPagingAndOrderParams();
            return build();
        }

        public String buildCountQuery() {
            count(tableName);
            return appendingFilteredParams();
        }

        public void setParams(PreparedStatement preparedStatement) throws SQLException {
            int k = 0;
            preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
            preparedStatement.setInt(++k, entityId);
            preparedStatement.setString(++k, AppointmentStatus.CANCELLED.name());
        }

        public void setParamsForEmailQuery(PreparedStatement preparedStatement) throws SQLException {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
        }

        public void setFilteredParams(PreparedStatement preparedStatement) throws SQLException {
            int k = 0;
            if (entityId != null) {
                preparedStatement.setInt(++k, entityId);
            }
            if (status != null) {
                preparedStatement.setString(++k, status.name());
            }
            if (paymentStatus != null) {
                preparedStatement.setString(++k, paymentStatus.name());
            }
            if (dateFrom != null) {
                preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(dateFrom, LocalTime.MIN)));
            }
            if (dateTo != null) {
                preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(dateTo, LocalTime.MIN)));
            }
        }

        private String appendingFilteredParams() {
            if (isOneFilteredParamNotNull()) {
                query.append(WHERE);
            }
            int count = 0;
            count = appendMasterId(count);
            count = appendStatus(count);
            appendDates(count);
            return build();
        }

        private boolean isOneFilteredParamNotNull() {
            return entityId != null || dateFrom != null || dateTo != null || status != null || paymentStatus != null;
        }

        private void appendPagingAndOrderParams() {
            orderBy(DATE).desc();
            pagination(page, size);
        }


        private int appendMasterId(int count) {
            if (entityId != null) {
                query.append(MASTER_ID).append(EQUAL);
                count++;
            }
            return count;
        }

        private int appendStatus(int count) {
            if (status != null) {
                appendAnd(query, count);
                count++;
                query.append(STATUS).append(EQUAL);
            }
            if (paymentStatus != null) {
                appendAnd(query, count);
                count++;
                query.append(PAYMENT_STATUS).append(EQUAL);
            }
            return count;
        }


        private void appendDates(int count) {
            if (dateFrom != null && dateTo != null) {
                appendAnd(query, count);
                query.append("DATE(date) >= ?").append(AND).append("DATE(date) <= ?");
            } else if (dateFrom != null) {
                query.append("DATE(date) >= ?");
            } else if (dateTo != null) {
                appendAnd(query, count);
                query.append("DATE(date) <= ?");
            }
        }

        private void appendAnd(StringBuilder q, int count) {
            if (count > 0) {
                q.append(AND);
            }
        }
    }
}
