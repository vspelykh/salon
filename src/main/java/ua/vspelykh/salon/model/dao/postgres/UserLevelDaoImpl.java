package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.QueryBuilder;
import ua.vspelykh.salon.model.dao.UserLevelDao;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.impl.UserLevelRowMapper;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static ua.vspelykh.salon.model.dao.Table.USER_LEVEL;
import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class is an implementation of the UserLevelDao interface and extends the AbstractDao class.
 * It provides methods to interact with the user_level table in the database.
 *
 * @version 1.0
 */
public class UserLevelDaoImpl extends AbstractDao<UserLevel> implements UserLevelDao {

    private static final Logger LOG = LogManager.getLogger(UserLevelDaoImpl.class);

    /**
     * Constructs a new UserLevelDaoImpl object.
     * It sets the RowMapper object and the table name for the user_level table.
     */
    public UserLevelDaoImpl() {
        super(new UserLevelRowMapper(), USER_LEVEL);
    }

    /**
     * Inserts a new user_level record into the database.
     *
     * @param entity UserLevel object containing the data to be inserted.
     * @return int the id of the newly created record.
     * @throws DaoException if there is an error while performing the operation.
     */
    @Override
    public int create(UserLevel entity) throws DaoException {
        String query = new QueryBuilder().insert(tableName, ID, LEVEL, ACTIVE, ABOUT, ABOUT + UA).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(statement, entity);
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
     * Updates an existing user_level record in the database.
     *
     * @param entity UserLevel object containing the data to be updated.
     * @throws DaoException if there is an error while performing the operation.
     */
    @Override
    public void update(UserLevel entity) throws DaoException {
        String query = new QueryBuilder().update(USER_LEVEL).set(LEVEL, ABOUT, ABOUT + UA, ACTIVE).where(ID).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            int k = 0;
            statement.setString(++k, entity.getLevel().toString());
            statement.setString(++k, entity.getAbout());
            statement.setString(++k, entity.getAboutUa());
            statement.setBoolean(++k, entity.isActive());
            statement.setInt(++k, entity.getMasterId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_UPDATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    /**
     * Sets the values of the prepared statement for the create() method.
     *
     * @param statement PreparedStatement object to be set.
     * @param userLevel UserLevel object containing the data to be set.
     * @throws SQLException if there is an error while setting the values.
     */
    private void setStatement(PreparedStatement statement, UserLevel userLevel) throws SQLException {
        int k = 0;
        statement.setInt(++k, userLevel.getMasterId());
        statement.setString(++k, userLevel.getLevel().name());
        statement.setBoolean(++k, userLevel.isActive());
        statement.setString(++k, userLevel.getAbout());
        statement.setString(++k, userLevel.getAboutUa());
    }

    /**
     * Retrieves a UserLevel object by its user id.
     *
     * @param userId int the user id to be searched for.
     * @return UserLevel object containing the data of the retrieved record.
     * @throws DaoException if there is an error while performing the operation.
     */
    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws DaoException {
        return findByParam(userId, Column.ID);
    }

    /**
     * Checks if a user_level record exists in the database by its user id.
     *
     * @param userId int the user id to be checked.
     * @return boolean true if the record exists, false otherwise.
     * @throws DaoException if there is an error while performing the operation.
     */
    @Override
    public boolean isExist(int userId) throws DaoException {
        String query = new QueryBuilder().exists(new QueryBuilder().selectFields(tableName, ID).where(ID).build()).build();
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            } else {
                throw new DaoException("Error to get information is userLevel exists ind DB");
            }
        } catch (SQLException e) {
            LOG.error(String.format("Error to get information is userLevel exists ind DB. Issue: %s", e.getMessage()));
            throw new DaoException(e);
        }
    }
}
