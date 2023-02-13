package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.util.exception.DaoException;

public interface InvitationDao extends Dao<Invitation> {

    Invitation findByEmail(String email) throws DaoException;

    void removeByEmailIfExists(String email) throws DaoException;
}
