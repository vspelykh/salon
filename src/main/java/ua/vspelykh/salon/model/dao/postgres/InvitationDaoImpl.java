package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.InvitationDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.impl.InvitationRowMapper;
import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * The InvitationDaoImpl class implements InvitationDao interface and extends AbstractDao class.
 * It provides implementation for CRUD operations on Invitation table in database.
 *
 * @version 1.0
 */
public class InvitationDaoImpl extends AbstractDao<Invitation> implements InvitationDao {

    private static final Logger LOG = LogManager.getLogger(InvitationDaoImpl.class);

    /**
     * Constructs an InvitationDaoImpl object and sets rowMapper and table name to its superclass.
     */
    public InvitationDaoImpl() {
        super(new InvitationRowMapper(), Table.INVITATION);
    }

    /**
     * Creates a new Invitation entity in the database.
     *
     * @param entity the Invitation object to be created
     * @return the auto-generated ID of the created entity
     * @throws DaoException if there is an error executing the query or generating the ID
     */
    @Override
    public int create(Invitation entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, EMAIL, ROLE, KEY).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setInvitationStatement(entity, statement);
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
     * Sets the values of the PreparedStatement for the given Invitation entity.
     *
     * @param entity    the Invitation object to set the PreparedStatement values for
     * @param statement the PreparedStatement to set the values on
     * @throws SQLException if there is an error setting the PreparedStatement values
     */
    private void setInvitationStatement(Invitation entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getEmail());
        statement.setString(++k, entity.getRole().name());
        statement.setString(++k, entity.getKey());
    }

    /**
     * This method throws an UnsupportedOperationException as the Invitation entity cannot be updated.
     *
     * @throws UnsupportedOperationException As Invitation entity cannot be updated
     */
    @Override
    public void update(Invitation entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * Finds an Invitation entity in the database by email.
     *
     * @param email the email address to search for
     * @return the Invitation object that matches the email, or null if none is found
     * @throws DaoException if there is an error executing the query
     */
    @Override
    public Invitation findByEmail(String email) throws DaoException {
        try {
            return findByParam(email, EMAIL);
        } catch (DaoException e) {
            LOG.error(String.format("%sin %s by email. Issue: %s", FAIL_FIND, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Removes an Invitation entity from the database by email, if it exists.
     *
     * @param email the email address to search for
     * @throws DaoException if there is an error executing the query or the item for deletion isn't found
     */
    @Override
    public void removeByEmailIfExists(String email) throws DaoException {
        String query = new QueryBuilder().delete(tableName).where(EMAIL).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setString(1, email);
            int i = statement.executeUpdate();
            if (i != 1) {
                LOG.info("Item for delete didn't find in {}", tableName);
            }
        } catch (SQLException e) {
            LOG.error("Fail to delete item.", e);
            throw new DaoException(e);
        }
    }
}
