package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.WorkingDayDao;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.SalonUtils.getDate;

/**
 * Implementation of the WorkingDayDao interface. This class handles the interactions with the database for
 * the WorkingDay table. Extends the AbstractDao class and implements the WorkingDayDao interface.
 *
 * @version 1.0
 */
public class WorkingDayDaoImpl extends AbstractDao<WorkingDay> implements WorkingDayDao {

    private static final Logger LOG = LogManager.getLogger(WorkingDayDaoImpl.class);

    /**
     * Constructor for WorkingDayDaoImpl class. Calls super() to instantiate the AbstractDao class and sets
     * the RowMapper and Table for the WorkingDay table.
     */
    public WorkingDayDaoImpl() {
        super(RowMapperFactory.getWorkingDayRowMapper(), Table.WORKING_DAY);
    }


    /**
     * Inserts a new WorkingDay entity into the database.
     *
     * @param entity The WorkingDay entity to insert into the database.
     * @return The auto-generated id of the newly inserted WorkingDay entity.
     * @throws DaoException If there is an error with the database connection or SQL statement.
     */
    @Override
    public int create(WorkingDay entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, USER_ID, DATE, TIME_START, TIME_END).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setWorkingDayStatement(entity, statement);
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
     * Sets the PreparedStatement parameters for inserting a new WorkingDay entity into the database.
     *
     * @param entity    The WorkingDay entity to insert into the database.
     * @param statement The PreparedStatement to set the parameters for.
     * @throws SQLException If there is an error setting the PreparedStatement parameters.
     */
    private void setWorkingDayStatement(WorkingDay entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getUserId());
        statement.setDate(++k, Date.valueOf(entity.getDate()));
        statement.setTime(++k, Time.valueOf(entity.getTimeStart()));
        statement.setTime(++k, Time.valueOf(entity.getTimeEnd()));
    }

    /**
     * @throws UnsupportedOperationException because updating WorkingDay is not allowed.
     */
    @Override
    public void update(WorkingDay entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * Saves a group of WorkingDay entities into the database.
     *
     * @param userId     The id of the user the WorkingDay entities belong to.
     * @param datesArray An array of dates representing the dates of the WorkingDay entities.
     * @param timeStart  The time the workday started.
     * @param timeEnd    The time the workday ended.
     * @throws DaoException If there is an error with the database connection or SQL statement.
     */
    @Override
    public void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws DaoException {
        WorkingDayQueryBuilder queryBuilder = new WorkingDayQueryBuilder(userId, datesArray, timeStart, timeEnd);
        String query = queryBuilder.buildSaveQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParamsForSave(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Error saving working days for user with id {}", userId);
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves all the working days for a given user ID from the database.
     *
     * @param userId the ID of the user for whom to retrieve the working days.
     * @return a List of WorkingDay objects representing the working days of the user.
     * @throws DaoException if there is an error accessing the database.
     */
    @Override
    public List<WorkingDay> getByUserId(Integer userId) throws DaoException {
        String query = new QueryBuilder().select(tableName).where(USER_ID).build();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<WorkingDay> workingDays = new ArrayList<>();
            while (resultSet.next()) {
                WorkingDay entity = rowMapper.map(resultSet);
                workingDays.add(entity);
            }
            return workingDays;
        } catch (SQLException e) {
            LOG.error(String.format("%s%s %d", FAIL_FIND_LIST, USER_ID, userId));
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves a single working day for a given user ID and date from the database.
     *
     * @param userId the ID of the user for whom to retrieve the working day.
     * @param date   the date of the working day to retrieve.
     * @return a WorkingDay object representing the specified working day.
     * @throws DaoException if there is an error accessing the database, or if the specified working day is not found.
     */
    @Override
    public WorkingDay getByUserIdAndDate(Integer userId, LocalDate date) throws DaoException {
        WorkingDay workingDay;
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.select(tableName).where(USER_ID).and(DATE);
        String query = new QueryBuilder().select(tableName).where(USER_ID).and(DATE).build();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            setWorkingDayStatement(preparedStatement, userId, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workingDay = rowMapper.map(resultSet);
                return workingDay;
            } else {
                LOG.error("No entity from {} found by {}.", tableName, userId);
                throw new DaoException("No entity");
            }
        } catch (SQLException e) {
            LOG.error(String.format("%s %s by userId and date", FAIL_FIND, tableName));
            throw new DaoException(e);
        }
    }

    /**
     * Deletes all working days for a given user ID and array of dates from the database.
     *
     * @param userId     the ID of the user whose working days should be deleted.
     * @param datesArray an array of date strings representing the dates of the working days to delete.
     * @throws DaoException if there is an error accessing the database.
     */
    @Override
    public void deleteByUserIdAndDatesArray(int userId, String[] datesArray) throws DaoException {
        WorkingDayQueryBuilder queryBuilder = new WorkingDayQueryBuilder(userId, datesArray);
        String query = queryBuilder.buildDeleteQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(String.format("%s working days by userId %d", FAIL_DELETE, userId));
            throw new DaoException(e);
        }
    }

    /**
     * Sets the ID and date parameters of a PreparedStatement object for retrieving a single working day.
     *
     * @param statement the PreparedStatement object to set the parameters on.
     * @param userId    the ID of the user for whom to retrieve the working day.
     * @param date      the date of the working day to retrieve.
     * @throws SQLException if there is an error setting the parameters on the PreparedStatement.
     */
    private void setWorkingDayStatement(PreparedStatement statement, Integer userId, LocalDate date) throws SQLException {
        int k = 0;
        statement.setInt(++k, userId);
        statement.setDate(++k, Date.valueOf(date));
    }

    /**
     * WorkingDayQueryBuilder is a class that extends QueryBuilder and provides methods for building SQL queries
     * related to working days.
     *
     * @version 1.0
     */
    private class WorkingDayQueryBuilder extends QueryBuilder {
        private final int userId;
        private final String[] datesArray;
        private Time timeStart;
        private Time timeEnd;

        /**
         * Constructs a new WorkingDayQueryBuilder instance with the given user ID and array of dates.
         * Uses to create delete query.
         *
         * @param userId     the user ID.
         * @param datesArray the array of dates.
         */
        public WorkingDayQueryBuilder(int userId, String[] datesArray) {
            this.userId = userId;
            this.datesArray = datesArray;
        }

        /**
         * Constructs a new WorkingDayQueryBuilder instance with the given user ID, array of dates,
         * start time, and end time.
         * Uses for create insert query.
         *
         * @param userId     the user ID.
         * @param datesArray the array of dates.
         * @param timeStart  the start time.
         * @param timeEnd    the end time.
         */
        public WorkingDayQueryBuilder(int userId, String[] datesArray, Time timeStart, Time timeEnd) {
            this.userId = userId;
            this.datesArray = datesArray;
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
        }

        /**
         * Builds the SQL delete query for working days.
         *
         * @return the SQL delete query.
         */
        private String buildDeleteQuery() {
            return delete(tableName).whereIn(DATE, datesArray.length).and(USER_ID).build();
        }

        /**
         * Builds the SQL save query for working days.
         *
         * @return the SQL save query.
         */
        private String buildSaveQuery() {
            return insertRepeat(tableName, datesArray.length, USER_ID, DATE, TIME_START, TIME_END)
                    .onConflict(USER_ID, DATE).doUpdate(TIME_START, TIME_END).build();
        }

        /**
         * Sets the parameters for the given PreparedStatement for the 'delete' query.
         *
         * @param preparedStatement the PreparedStatement to set the parameters for.
         * @throws SQLException if a database access error occurs.
         */
        private void setParams(PreparedStatement preparedStatement) throws SQLException {
            int paramNum = 1;
            if (datesArray.length != 0) {
                for (String date : datesArray) {
                    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    preparedStatement.setDate(paramNum++, Date.valueOf(localDate));
                }
            }
            preparedStatement.setInt(paramNum, userId);
        }

        /**
         * Sets the parameters for the given PreparedStatement for the save query.
         *
         * @param preparedStatement the PreparedStatement to set the parameters for.
         * @throws SQLException if a database access error occurs.
         */
        private void setParamsForSave(PreparedStatement preparedStatement) throws SQLException {
            int paramNum = 0;
            for (String date : datesArray) {
                preparedStatement.setInt(++paramNum, userId);
                preparedStatement.setDate(++paramNum, getDate(date));
                preparedStatement.setTime(++paramNum, timeStart);
                preparedStatement.setTime(++paramNum, timeEnd);
            }
            preparedStatement.setTime(++paramNum, timeStart);
            preparedStatement.setTime(++paramNum, timeEnd);
        }
    }
}
