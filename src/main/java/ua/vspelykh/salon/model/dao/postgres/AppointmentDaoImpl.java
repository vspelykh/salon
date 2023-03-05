package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.impl.AppointmentRowMapper;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.util.AppointmentFilter;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * The AppointmentDaoImpl class is an implementation of the AppointmentDao interface.
 * It provides methods to perform CRUD operations on the Appointment table in the database.
 *
 * @version 1.0
 */
public class AppointmentDaoImpl extends AbstractDao<Appointment> implements AppointmentDao {

    private static final Logger LOG = LogManager.getLogger(AppointmentDaoImpl.class);

    /**
     * Constructs a new AppointmentDaoImpl with the given row mapper and table name.
     */
    public AppointmentDaoImpl() {
        super(new AppointmentRowMapper(), Table.APPOINTMENT);
    }

    /**
     * Returns a list of all appointments scheduled for a specific date.
     *
     * @param date the date for which to retrieve appointments.
     * @return a List of Appointment objects scheduled for the specified date.
     * @throws DaoException if there was an error executing the query.
     */
    @Override
    public List<Appointment> getAllByDate(LocalDate date) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(date);
        String query = queryBuilder.buildDateQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParamsForDateQuery(preparedStatement);
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

    /**
     * Retrieves a list of Appointment filtered by the given AppointmentFilter and paging parameters:
     * page number and size of page.
     *
     * @param filter the AppointmentFilter to use as a filter
     * @param page   the number of the requested page
     * @param size   the size of the requested page
     * @return a list of Appointment entities that match the given filter and paging parameters
     * @throws DaoException if there is an issue accessing the data source or executing the query
     */
    @Override
    public List<Appointment> getFiltered(AppointmentFilter filter, int page, int size) throws DaoException {
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(filter, page, size);
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

    /**
     * Returns the count of appointments in the database that match the provided filter.
     *
     * @param filter The filter to apply when counting appointments.
     * @return The count of appointments that match the filter.
     * @throws DaoException If an error occurs while executing the query.
     */
    @Override
    public int getCountOfAppointments(AppointmentFilter filter) throws DaoException {
        int count;
        AppointmentQueryBuilder queryBuilder = new AppointmentQueryBuilder(filter);
        String query = queryBuilder.buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setFilteredParams(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            } else {
                if (LOG.isEnabled(Level.ERROR)) {
                    LOG.error(String.format("%s%s", FAIL_COUNT, tableName));
                }
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
        return count;
    }

    /**
     * Returns a list of appointments for a given date and master ID.
     *
     * @param date     the date of the appointments
     * @param masterId the ID of the master associated with the appointments
     * @return a list of appointments for the given date and master ID
     * @throws DaoException if there is an error executing the database query
     */
    @Override
    public List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws DaoException {
        return getByDateAndEntityId(date, masterId, Column.MASTER_ID);
    }

    /**
     * Retrieves appointments by a given date and entity ID.
     *
     * @param date     the date of the appointments.
     * @param entityId the ID of the entity (master or client).
     * @param column   the name of the column in the database table that corresponds to the entity (master or client).
     * @return a list of appointments.
     * @throws DaoException if there is an issue accessing the database.
     */
    @SuppressWarnings("SameParameterValue")
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

    /**
     * Creates a new appointment in the database.
     *
     * @param entity the appointment to be created
     * @return the id of the newly created appointment
     * @throws DaoException if there is an issue with the database connection or the SQL query
     */
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

    /**
     * Updates an existing appointment in the database.
     *
     * @param entity The appointment entity to be updated.
     * @throws DaoException If there is an error executing the SQL query or the appointment with the given ID is not found.
     */
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

    /**
     * Sets the parameters of the given PreparedStatement with the values from the Appointment entity.
     *
     * @param entity    the Appointment entity to get values from
     * @param statement the PreparedStatement to set values to
     * @return the index of the last parameter that was set
     * @throws SQLException if a database access error occurs or this method is called on a closed PreparedStatement
     */
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

    /**
     * The AppointmentQueryBuilder class provides a query builder for constructing SQL queries related to appointments.
     *
     * @version 1.0
     */
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

        /**
         * Constructs an AppointmentQueryBuilder with the given date, entity ID, and column name.
         *
         * @param date       the date for which to retrieve appointments
         * @param entityId   the ID of the entity to which the appointment belongs
         * @param columnName the name of the column by which to order appointments
         */
        public AppointmentQueryBuilder(LocalDate date, int entityId, String columnName) {
            this.date = date;
            this.entityId = entityId;
            this.columnName = columnName;
        }

        /**
         * Constructs an AppointmentQueryBuilder with the given date.
         *
         * @param date the date for which to retrieve appointments
         */
        public AppointmentQueryBuilder(LocalDate date) {
            this.date = date;
        }

        /**
         * Constructs an AppointmentQueryBuilder with the given filter, page number, and page size.
         *
         * @param filter the filter to apply to appointments
         * @param page   the page number for pagination
         * @param size   the number of records to return per page
         */
        public AppointmentQueryBuilder(AppointmentFilter filter, int page, int size) {
            this.entityId = filter.getMasterId();
            this.dateFrom = filter.getDateFrom();
            this.dateTo = filter.getDateTo();
            this.status = filter.getStatus();
            this.paymentStatus = filter.getPaymentStatus();
            this.page = page;
            this.size = size;
        }

        /**
         * Constructs an AppointmentQueryBuilder with the given filter.
         * Uses for build count query.
         *
         * @param filter the filter to apply to appointments
         */
        public AppointmentQueryBuilder(AppointmentFilter filter) {
            this.entityId = filter.getMasterId();
            this.dateFrom = filter.getDateFrom();
            this.dateTo = filter.getDateTo();
            this.status = filter.getStatus();
            this.paymentStatus = filter.getPaymentStatus();
        }

        /**
         * Builds simple query string for retrieving appointments.
         *
         * @return the query string
         */
        public String buildQuery() {
            return append(dateQuery).and(columnName).andNotEqual(STATUS).orderBy(DATE).build();
        }

        /**
         * Builds the query string for retrieving appointments by date.
         *
         * @return the query string
         */
        public String buildDateQuery() {
            return dateQuery;
        }

        /**
         * Builds the query string for retrieving filtered appointments.
         *
         * @return the query string
         */
        public String buildFilteredQuery() {
            select(tableName);
            appendingFilteredParams();
            appendPagingAndOrderParams();
            return build();
        }

        /**
         * Builds the query string for retrieving the count of filtered appointments.
         *
         * @return the query string
         */
        public String buildCountQuery() {
            count(tableName);
            return appendingFilteredParams();
        }

        /**
         * Sets the parameters for the appointment query.
         *
         * @param preparedStatement the prepared statement to which to set the parameters
         * @throws SQLException if an error occurs while setting the parameters
         */
        public void setParams(PreparedStatement preparedStatement) throws SQLException {
            int k = 0;
            preparedStatement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
            preparedStatement.setInt(++k, entityId);
            preparedStatement.setString(++k, AppointmentStatus.CANCELLED.name());
        }

        /**
         * Sets the parameters for the appointment date query.
         *
         * @param preparedStatement the prepared statement to which to set the parameters
         * @throws SQLException if an error occurs while setting the parameters
         */
        public void setParamsForDateQuery(PreparedStatement preparedStatement) throws SQLException {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)));
        }

        /**
         * Sets the parameters for the appointment filter query.
         *
         * @param preparedStatement the prepared statement to which to set the parameters
         * @throws SQLException if an error occurs while setting the parameters
         */
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

