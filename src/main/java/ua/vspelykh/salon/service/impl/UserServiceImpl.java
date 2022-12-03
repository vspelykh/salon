package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.UserLevelDao;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;
import ua.vspelykh.salon.service.UserLevelService;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

import static ua.vspelykh.salon.util.validation.Validation.checkUser;

public class UserServiceImpl implements UserService, UserLevelService {

    private final UserDao userDao;
    private final UserLevelDao userLevelDao;

    public UserServiceImpl() {
        userDao = DaoFactory.getUserDao();
        userLevelDao = DaoFactory.getUserLevelDao();
    }

    @Override
    public User findById(Integer id) throws ServiceException {
        try {
            return userDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Can't find a user with id " + id, e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws ServiceException {
        try {
            return userDao.findByEmailAndPassword(email, password);
        } catch (DaoException e) {
            throw new ServiceException("Login or password is not correct", e);
        }
    }

    @Override
    public User findByNumber(String number) throws ServiceException {
        try {
            return userDao.findByNumber(number);
        } catch (DaoException e) {
            throw new ServiceException("User with number " + number + " don't find", e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findClients() throws ServiceException {
        try {
            return userDao.findClients();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findMasters() throws ServiceException {
        try {
            return userDao.findMasters();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAdministrators() throws ServiceException {
        try {
            return userDao.findAdministrators();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(User user) throws ServiceException {
        try {
            checkUser(user);
            if (user.isNew()) {
                userDao.create(user);
            } else userDao.update(user);
        } catch (DaoException e) {
            //log
            throw new ServiceException(e.getMessage(), e);
        }
    }


    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            userDao.removeById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getUsersByLevel(UserLevel userLevel) throws ServiceException {
        try {
            return userLevelDao.getUsersByLevel(userLevel);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws ServiceException {
        try {
            return userLevelDao.getUserLevelByUserId(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void create(UserLevel userLevel) throws ServiceException {
        try {
            userLevelDao.create(userLevel);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(UserLevel userLevel) throws ServiceException {
        try {
            userLevelDao.update(userLevel);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
