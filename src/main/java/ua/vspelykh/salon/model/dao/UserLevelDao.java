package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface UserLevelDao extends Dao<UserLevel> {

    List<User> getUsersByLevel(UserLevel userLevel, boolean isActive) throws DaoException;

    UserLevel getUserLevelByUserId(Integer userId) throws DaoException;

}
