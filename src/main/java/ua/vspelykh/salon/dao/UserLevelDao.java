package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface UserLevelDao extends Dao<UserLevel> {

    List<User> getUsersByLevel(UserLevel userLevel, boolean isActive) throws DaoException;

    UserLevel getUserLevelByUserId(Integer userId) throws DaoException;

}
