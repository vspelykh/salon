package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENTS;
import static ua.vspelykh.salon.model.dao.Table.FEEDBACKS;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

public class FeedbackDaoImpl extends AbstractDao<Feedback> implements FeedbackDao {

    private static final Logger LOG = LogManager.getLogger(FeedbackDaoImpl.class);

    public FeedbackDaoImpl() {
        super(RowMapperFactory.getMarkRowMapper(), FEEDBACKS);
    }

    @Override
    public int create(Feedback entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, APPOINTMENT_ID, MARK, COMMENT, DATE).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setMarkStatement(entity, statement);
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

    private void setMarkStatement(Feedback entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getAppointmentId());
        statement.setInt(++k, entity.getMark());
        statement.setString(++k, entity.getComment());
        statement.setTimestamp(++k, Timestamp.valueOf(entity.getDate()));
    }

    @Override
    public void update(Feedback entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Feedback> getFeedbacksByMasterId(Integer masterId, int page) throws DaoException {
        String query = new MarkQueryBuilder(page).buildQuery();
        return getFeedbacks(query, masterId);
    }

    @Override
    public List<Feedback> getFeedbacksByMasterId(Integer masterId) throws DaoException {
        String query = new MarkQueryBuilder().buildQuery();
        return getFeedbacks(query, masterId);
    }

    private List<Feedback> getFeedbacks(String query, Integer masterId) throws DaoException {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, masterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Feedback> marks = new ArrayList<>();
            while (resultSet.next()) {
                Feedback entity = rowMapper.map(resultSet);
                marks.add(entity);
            }
            return marks;
        } catch (SQLException e) {
            LOG.error(String.format("%s%s by masterId. Issue: %s", FAIL_FIND_LIST, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public int countFeedbacksByMasterId(Integer masterId) throws DaoException {
        String query = new MarkQueryBuilder().buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, masterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                LOG.error(String.format("%s%s", FAIL_COUNT, tableName));
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    @Override
    public Feedback findByAppointmentId(Integer appointmentId) throws DaoException {
        return findByParam(appointmentId, APPOINTMENT_ID);
    }

    private class MarkQueryBuilder extends QueryBuilder {

        private Integer page;

        public MarkQueryBuilder(int page) {
            this.page = page;
        }

        public MarkQueryBuilder() {

        }

        public String buildQuery() {
            select(tableName);
            whereInCondition(APPOINTMENT_ID, new QueryBuilder().selectFields(APPOINTMENTS, ID).where(MASTER_ID).build());
            orderBy(DATE).desc();
            if (page != null) {
                pagination(page, 5);
            }
            return build();
        }

        public String buildCountQuery() {
            String condition = new QueryBuilder().selectFields(APPOINTMENTS, ID).where(MASTER_ID).build();
            return count(tableName).whereInCondition(APPOINTMENT_ID, condition).build();
        }
    }
}
