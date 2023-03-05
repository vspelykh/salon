package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.ConsultationDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.impl.ConsultationRowMapper;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static ua.vspelykh.salon.model.dao.mapper.Column.NAME;
import static ua.vspelykh.salon.model.dao.mapper.Column.NUMBER;

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
        super(new ConsultationRowMapper(), Table.CONSULTATION);
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
     * This method throws an UnsupportedOperationException as the Consultation entity cannot be updated.
     *
     * @throws UnsupportedOperationException As Consultation entity cannot be updated
     */
    @Override
    public void update(Consultation entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
