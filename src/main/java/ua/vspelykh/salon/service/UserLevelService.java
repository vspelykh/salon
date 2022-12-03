package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface UserLevelService {

    List<User> getUsersByLevel(UserLevel userLevel) throws ServiceException;

    UserLevel getUserLevelByUserId(Integer userId) throws ServiceException;

    void create(UserLevel userLevel) throws ServiceException;

    void update(UserLevel userLevel) throws ServiceException;

}
