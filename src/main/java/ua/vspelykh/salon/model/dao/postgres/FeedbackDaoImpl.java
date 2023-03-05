package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.mapper.impl.FeedbackRowMapper;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.command.CommandNames.APPOINTMENTS;
import static ua.vspelykh.salon.model.dao.Table.FEEDBACKS;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * FeedbackDaoImpl class is an implementation of the FeedbackDao interface.
 * It provides methods for interacting with the database to perform CRUD operations on Feedback objects.
 *
 * @version 1.0
 */
public class FeedbackDaoImpl extends AbstractDao<Feedback> implements FeedbackDao {

    private static final Logger LOG = LogManager.getLogger(FeedbackDaoImpl.class);

    /**
     * Constructor for creating an instance of the FeedbackDaoImpl class.
     * Sets the row mapper and table name for the super class.
     */
    public FeedbackDaoImpl() {
        super(new FeedbackRowMapper(), FEEDBACKS);
    }

    /**
     * Creates a new Feedback object in the database.
     *
     * @param entity The Feedback object to be created.
     * @return The ID of the newly created Feedback object.
     * @throws DaoException If an error occurs while creating the Feedback object.
     */
    @Override
    public int create(Feedback entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, APPOINTMENT_ID, MARK, COMMENT, DATE).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setFeedbackStatement(entity, statement);
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
     * Sets the values of the given Feedback entity into the provided PreparedStatement.
     *
     * @param entity    the entity to be set
     * @param statement the prepared statement to set the values into
     * @throws SQLException if an error occurs while setting the values into the statement
     */
    private void setFeedbackStatement(Feedback entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getAppointmentId());
        statement.setInt(++k, entity.getMark());
        statement.setString(++k, entity.getComment());
        statement.setTimestamp(++k, Timestamp.valueOf(entity.getDate()));
    }

    /**
     * This method throws an UnsupportedOperationException as the Feedback entity cannot be updated.
     *
     * @throws UnsupportedOperationException As Feedback entity cannot be updated
     */
    @Override
    public void update(Feedback entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves a list of Feedback objects from the database by master ID and page number.
     *
     * @param masterId The ID of the master to retrieve Feedback objects for.
     * @param page     The page number to retrieve Feedback objects for.
     * @return A List of Feedback objects for the given master ID and page number.
     * @throws DaoException If an error occurs while retrieving the Feedback objects.
     */
    @Override
    public List<Feedback> getFeedbacksByMasterId(Integer masterId, int page) throws DaoException {
        String query = new MarkQueryBuilder(page).buildQuery();
        return getFeedbacks(query, masterId);
    }

    /**
     * Retrieves a list of Feedback objects from the database by master ID.
     *
     * @param masterId The ID of the master to retrieve Feedback objects for.
     * @return A List of Feedback objects for the given master ID.
     * @throws DaoException If an error occurs while retrieving the Feedback objects.
     */
    @Override
    public List<Feedback> getFeedbacksByMasterId(Integer masterId) throws DaoException {
        String query = new MarkQueryBuilder().buildQuery();
        return getFeedbacks(query, masterId);
    }

    /**
     * Retrieves a list of Feedbacks based on the provided masterId
     *
     * @param query    the SQL query string to be executed
     * @param masterId the ID of the master to search Feedbacks for
     * @return a list of Feedback objects that match the masterId
     * @throws DaoException if there is an issue executing the SQL query or mapping the results
     */
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

    /**
     * Counts the number of Feedback objects in the database for a given master ID.
     *
     * @param masterId The ID of the master to count Feedback objects for.
     * @return The number of Feedback objects for the given master ID.
     * @throws DaoException If an error occurs while counting the Feedback objects.
     */
    @Override
    public int countByMasterId(Integer masterId) throws DaoException {
        String query = new MarkQueryBuilder().buildCountQuery();
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, masterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                if (LOG.isEnabled(Level.ERROR)) {
                    LOG.error(String.format(LOG_FORMAT, FAIL_COUNT, tableName));
                }
                throw new DaoException(FAIL_COUNT + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_COUNT, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Retrieves a Feedback object from the database by appointment ID.
     *
     * @param appointmentId The ID of the appointment to retrieve the Feedback object for.
     * @return The Feedback object for the given appointment ID.
     * @throws DaoException If an error occurs while retrieving the Feedback object.
     */
    @Override
    public Feedback findByAppointmentId(Integer appointmentId) throws DaoException {
        return findByParam(appointmentId, APPOINTMENT_ID);
    }

    /**
     * The MarkQueryBuilder class provides methods for building SQL queries for retrieving Feedback objects.
     * It extends the QueryBuilder class and adds methods specific to Feedback retrieval.
     */
    private class MarkQueryBuilder extends QueryBuilder {

        private Integer page;

        /**
         * Constructs a new MarkQueryBuilder with the specified page number for pagination.
         *
         * @param page the page number for pagination
         */
        public MarkQueryBuilder(int page) {
            this.page = page;
        }

        /**
         * Constructs a new MarkQueryBuilder without pagination.
         */
        public MarkQueryBuilder() {

        }

        /**
         * Builds a SQL query for retrieving Feedback objects.
         * The query selects from the Feedback table, filters by Appointment IDs associated with the Master ID,
         * orders by date in descending order, and applies pagination if specified.
         *
         * @return the built SQL-query as a String
         */
        public String buildQuery() {
            select(tableName);
            whereInCondition(APPOINTMENT_ID, new QueryBuilder().selectFields(APPOINTMENTS, ID).where(MASTER_ID).build());
            orderBy(DATE).desc();
            if (page != null) {
                pagination(page, 5);
            }
            return build();
        }

        /**
         * Builds a SQL query for counting the number of Feedback objects.
         * The query counts the number of rows in the Feedback table filtered by Appointment IDs associated with the Master ID.
         *
         * @return the built SQL-query as a String
         */
        public String buildCountQuery() {
            String condition = new QueryBuilder().selectFields(APPOINTMENTS, ID).where(MASTER_ID).build();
            return count(tableName).whereInCondition(APPOINTMENT_ID, condition).build();
        }
    }
}
