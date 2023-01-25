package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.WorkingDayDao;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.dao.mapper.Column.*;
import static ua.vspelykh.salon.util.SalonUtils.getDate;

public class WorkingDayDaoImpl extends AbstractDao<WorkingDay> implements WorkingDayDao {

    private static final Logger LOG = LogManager.getLogger(WorkingDayDaoImpl.class);

    public WorkingDayDaoImpl() {
        super(RowMapperFactory.getWorkingDayRowMapper(), Table.WORKING_DAY);
    }

    @Override
    public int create(WorkingDay entity) throws DaoException {
        String query = INSERT + tableName + " (id, user_id, date, time_start, time_end)"
                + VALUES + "(?,?,?,?,?)" + "AND date >= CURRENT_DATE-1";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setWorkingDayStatement(entity, statement);
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

    private void setWorkingDayStatement(WorkingDay entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getId());
        statement.setInt(++k, entity.getUserId());
        statement.setTimestamp(++k, Timestamp.valueOf(LocalDateTime.of(entity.getDate(), LocalTime.MIN)));
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
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParamsForSave(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public List<WorkingDay> getWorkingDaysByUserId(Integer userId) throws DaoException {
        String query = SELECT + tableName + WHERE + USER_ID + EQUAL;
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<WorkingDay> workingDays = new ArrayList<>();
            while (resultSet.next()) {
                WorkingDay entity = rowMapper.map(resultSet);
                workingDays.add(entity);
            }
            return workingDays;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    @Override
    public WorkingDay getDayByUserIdAndDate(Integer userId, LocalDate date) throws DaoException {
        WorkingDay workingDay;
        String query = SELECT + tableName + " WHERE user_id=? AND date=?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setIdAndDataWorkingDayStatement(preparedStatement, userId, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                workingDay = rowMapper.map(resultSet);
                return workingDay;
            } else {
//                LOG.error("No entity from {} found by {} with password.", tableName, email);
                throw new DaoException("No entity");
            }
        } catch (SQLException e) {
//            LOG.error(String.format("%s %s by email and password", FAIL_FIND, tableName));
            throw new DaoException(FAIL_FIND + tableName);
        }
    }

    @Override
    public void deleteWorkingDaysByUserIdAndDatesArray(int userId, String[] datesArray) throws DaoException {
        WorkingDayQueryBuilder queryBuilder = new WorkingDayQueryBuilder(userId, datesArray);
        String query = queryBuilder.buildDeleteQuery();
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            queryBuilder.setParams(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    private void setIdAndDataWorkingDayStatement(PreparedStatement statement, Integer userId, LocalDate date) throws SQLException {
        int k = 0;
        statement.setInt(++k, userId);
        statement.setDate(++k, Date.valueOf(date));
    }


    private class WorkingDayQueryBuilder {
        private int userId;
        private String[] datesArray;
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
            StringBuilder query = new StringBuilder(DELETE).append(tableName).append(WHERE).append(DATE);
            query.append(" IN(");
            appendQuestionMarks(query, datesArray);
            query.append(AND).append(USER_ID).append(EQUAL);
            return query.toString();
        }

        private String buildSaveQuery() {
            StringBuilder query = new StringBuilder(INSERT).append(tableName);
            query.append(" (user_id, date, time_start, time_end)").append(VALUES).append("(?,?,?,?), ".repeat(datesArray.length));
            query.replace(query.length() - 2, query.length(), "");
            query.append(ON_CONFLICT).append("(user_id, date)").append(DO_UPDATE);
            query.append(SET).append(TIME_START).append(EQUAL).append(", ").append(TIME_END).append(EQUAL);
            return query.toString();
        }

        private void appendQuestionMarks(StringBuilder query, String[] datesArray) {
            for (int i = 0; i < datesArray.length; i++) {
                query.append("?");
                if (i != datesArray.length - 1)
                    query.append(",");
            }
            query.append(")");
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
