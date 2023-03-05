package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

/**
 * This interface represents a Data Access Object (DAO) for managing user level data in the database.
 *
 * @version 1.0
 */
public interface UserLevelDao extends Dao<UserLevel> {

    /**
     * Retrieves the user level for a given user ID.
     *
     * @param userId the ID of the user to retrieve the level for.
     * @return the user level associated with the given user ID.
     * @throws DaoException if there is an error executing the query or processing the results.
     */
    UserLevel getUserLevelByUserId(Integer userId) throws DaoException;

    /**
     * Checks if there exists a user level with the given user ID.
     *
     * @param userId the ID of the user to check the existence of the level for.
     * @return true if a user level with the given user ID exists, false otherwise.
     * @throws DaoException if there is an error executing the query or processing the results.
     */
    boolean isExist(int userId) throws DaoException;
}
