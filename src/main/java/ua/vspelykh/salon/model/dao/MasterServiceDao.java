package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

/**
 * The MasterServiceDao interface provides methods to work with master service objects in the database.
 *
 * @version 1.0
 */
public interface MasterServiceDao extends Dao<MasterService> {

    /**
     * Retrieves a list of all master services associated with a given user ID.
     *
     * @param userId the ID of the user to retrieve master services for.
     * @return a list of all master services associated with the user.
     * @throws DaoException if there is an error accessing the database.
     */
    List<MasterService> getAllByUserId(Integer userId) throws DaoException;
}
