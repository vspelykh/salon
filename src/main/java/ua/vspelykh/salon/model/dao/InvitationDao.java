package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.util.exception.DaoException;

/**
 * The InvitationDao interface provides methods to work with invitation objects in the database.
 *
 * @version 1.0
 */
public interface InvitationDao extends Dao<Invitation> {

    /**
     * Finds an invitation by email.
     *
     * @param email the email of the invitation to find.
     * @return the found invitation, or null if not found.
     * @throws DaoException if there is an error accessing the database.
     */
    Invitation findByEmail(String email) throws DaoException;

    /**
     * Removes an invitation by email if it exists.
     *
     * @param email the email of the invitation to remove.
     * @throws DaoException if there is an error accessing the database.
     */
    void removeByEmailIfExists(String email) throws DaoException;
}
