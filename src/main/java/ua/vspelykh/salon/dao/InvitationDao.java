package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.Invitation;
import ua.vspelykh.salon.util.exception.DaoException;

public interface InvitationDao extends Dao<Invitation> {

    Invitation findByEmail(String email) throws DaoException;

    void removeByEmailIfExists(String email) throws DaoException;
}
