package ua.vspelykh.salon.model.dao.impl;

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

public class WorkingDayDaoImpl extends AbstractDao<WorkingDay> implements WorkingDayDao {

    private static final Logger LOG = LogManager.getLogger(WorkingDayDaoImpl.class);

    public WorkingDayDaoImpl() {
        super(RowMapperFactory.getWorkingDayRowMapper(), Table.WORKING_DAY);
    }

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

    private void setWorkingDayStatement(WorkingDay entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getUserId());
        statement.setDate(++k, Date.valueOf(entity.getDate()));
        statement.setTime(++k, Time.valueOf(entity.getTimeStart()));
        statement.setTime(++k, Time.valueOf(entity.getTimeEnd()));
    }

    @Override
    public void update(WorkingDay entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

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

    @Override
    public List<WorkingDay> getWorkingDaysByUserId(Integer userId) throws DaoException {
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

    @Override
    public WorkingDay getDayByUserIdAndDate(Integer userId, LocalDate date) throws DaoException {
        WorkingDay workingDay;
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.select(tableName).where(USER_ID).and(DATE);
        String query = new QueryBuilder().select(tableName).where(USER_ID).and(DATE).build();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            setIdAndDataWorkingDayStatement(preparedStatement, userId, date);
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

    @Override
    public void deleteWorkingDaysByUserIdAndDatesArray(int userId, String[] datesArray) throws DaoException {
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

    private void setIdAndDataWorkingDayStatement(PreparedStatement statement, Integer userId, LocalDate date) throws SQLException {
        int k = 0;
        statement.setInt(++k, userId);
        statement.setDate(++k, Date.valueOf(date));
    }

    private class WorkingDayQueryBuilder extends QueryBuilder {
        private final int userId;
        private final String[] datesArray;
        private Time timeStart;
        private Time timeEnd;

        public WorkingDayQueryBuilder(int userId, String[] datesArray) {
            this.userId = userId;
            this.datesArray = datesArray;
        }

        public WorkingDayQueryBuilder(int userId, String[] datesArray, Time timeStart, Time timeEnd) {
            this.userId = userId;
            this.datesArray = datesArray;
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
        }

        private String buildDeleteQuery() {
            return delete(tableName).whereIn(DATE, datesArray.length).and(USER_ID).build();
        }

        private String buildSaveQuery() {
            return insertRepeat(tableName, datesArray.length, USER_ID, DATE, TIME_START, TIME_END)
                    .onConflict(USER_ID, DATE).doUpdate(TIME_START, TIME_END).build();
        }

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
