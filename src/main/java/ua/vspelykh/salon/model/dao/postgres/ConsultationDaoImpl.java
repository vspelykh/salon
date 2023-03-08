package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.ConsultationDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.mapper.impl.ConsultationRowMapper;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.model.dao.Table.CONSULTATION;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * ConsultationDaoImpl is a concrete implementation of the ConsultationDao interface that extends
 * the AbstractDao abstract class.
 * It provides methods for interacting with the "consultations" table in the database.
 *
 * @version 1.0
 */
public class ConsultationDaoImpl extends AbstractDao<Consultation> implements ConsultationDao {

    private static final Logger LOG = LogManager.getLogger(ConsultationDaoImpl.class);

    /**
     * Constructs a new ConsultationDaoImpl with a specific RowMapper and table name.
     */
    public ConsultationDaoImpl() {
        super(new ConsultationRowMapper(), CONSULTATION);
    }

    /**
     * Creates a new consultation record in the database.
     *
     * @param entity the Consultation object to be created
     * @return the generated ID of the newly created record
     * @throws DaoException if there is an error executing the SQL query or retrieving the generated key
     */
    @Override
    public int create(Consultation entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, NAME, NUMBER).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(entity, statement);
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
     * Sets the values of the given Consultation entity into the provided PreparedStatement.
     *
     * @param entity    the entity to be set
     * @param statement the prepared statement to set the values into
     * @throws SQLException if an error occurs while setting the values into the statement
     */
    private void setStatement(Consultation entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getNumber());
    }

    /**
     * This method updates a Consultation entity in the database.
     *
     * @param entity The Consultation entity to be updated
     * @throws DaoException if there is an error executing the update statement or the entity cannot be updated
     */
    @Override
    public void update(Consultation entity) throws DaoException {
        String query = new QueryBuilder().update(CONSULTATION).set(NAME, NUMBER, DATE, IS_READ).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            setUpdateStatement(entity, statement);
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
            throw new DaoException(FAIL_UPDATE + tableName, e);
        }
    }

    private void setUpdateStatement(Consultation entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getNumber());
        statement.setTimestamp(++k, Timestamp.valueOf(entity.getDate()));
        statement.setBoolean(++k, entity.isRead());
        statement.setInt(++k, entity.getId());
    }

    /**
     * Retrieves a list of new consultations from the database.
     *
     * @return a list of Consultation objects that have not been processed yet
     * @throws DaoException if an error occurs while accessing the persistence layer
     */
    @Override
    public List<Consultation> getNewConsultations() throws DaoException {
        String query = new QueryBuilder().select(CONSULTATION).where(IS_READ).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setBoolean(1, false);
            ResultSet resultSet = statement.executeQuery();
            List<Consultation> consultations = new ArrayList<>();
            while (resultSet.next()) {
                consultations.add(rowMapper.map(resultSet));
            }
            return consultations;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }
}
