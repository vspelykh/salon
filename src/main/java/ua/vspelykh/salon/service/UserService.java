package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface UserService {

    User findById(Integer id) throws ServiceException;

    User findByEmailAndPassword(String email, String password) throws ServiceException;

    User findByNumber(String number) throws ServiceException;

    List<User> findAll() throws ServiceException;

    List<User> findClients() throws ServiceException;

    List<User> findMasters(boolean isActive) throws ServiceException;

    List<User> findAdministrators() throws ServiceException;

    List<User> getUsersByLevel(UserLevel userLevel, boolean isActive) throws ServiceException;

    void save(User user) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    UserLevel getUserLevelByUserId(Integer userId) throws ServiceException;

    void create(UserLevel userLevel) throws ServiceException;

    void update(UserLevel userLevel) throws ServiceException;

}