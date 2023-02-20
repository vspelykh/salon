package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

public interface UserLevelDao extends Dao<UserLevel> {

    UserLevel getUserLevelByUserId(Integer userId) throws DaoException;

    boolean isExist(int userId) throws DaoException;
}
