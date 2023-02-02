package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.dao.MarkDao;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.UserLevelDao;
import ua.vspelykh.salon.dto.UserMasterDTO;
import ua.vspelykh.salon.model.*;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.command.user.ChangeRoleCommand.ADD;
import static ua.vspelykh.salon.controller.command.user.ChangeRoleCommand.REMOVE;
import static ua.vspelykh.salon.util.validation.Validation.checkUser;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private UserLevelDao userLevelDao;
    private MarkDao markDao;

    @Override
    public User findById(Integer id) throws ServiceException {
        try {
            return userDao.findById(id);
        } catch (DaoException e) {
            LOG.error(String.format("Can't find a user with id %d", id));
            throw new ServiceException(e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws ServiceException {
        try {
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            User user = userDao.findByEmail(email);
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                return user;
            } else throw new ServiceException("Incorrect username or password");
        } catch (DaoException e) {
            throw new ServiceException("Incorrect username or password", e);
        }
    }

    @Override
    public User findByNumber(String number) throws ServiceException {
        try {
            return userDao.findByNumber(number);
        } catch (DaoException e) {
            LOG.error(String.format("User with number %s didn't find", number));
            throw new ServiceException(e);
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
    public List<User> findMasters(boolean isActive) throws ServiceException {
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
            } else {
                userDao.update(user);
            }
        } catch (DaoException e) {
            LOG.error("Error to save user");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            userDao.removeById(id);
        } catch (DaoException e) {
            LOG.error("Error to delete user by id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> getUsersByLevel(UserLevel userLevel, boolean isActive) throws ServiceException {
        try {
            return userLevelDao.getUsersByLevel(userLevel, isActive);
        } catch (DaoException e) {
            LOG.error("Unable to get users by user level");
            throw new ServiceException(e);
        }
    }


    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws ServiceException {
        try {
            return userLevelDao.getUserLevelByUserId(userId);
        } catch (DaoException e) {
            LOG.error("User must have a master role");
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

    @Override
    public List<UserMasterDTO> getMastersDto(List<MastersLevel> levels, List<Integer> serviceIds,
                                             List<Integer> categoriesIds, String search, int page, int size, MasterSort sort, String locale) throws ServiceException {
        try {
            List<UserMasterDTO> dtos = new ArrayList<>();
            List<User> masters = userDao.findMastersByLevelsAndServices(levels, serviceIds, categoriesIds, search, page, size, sort);

            for (User currentMaster : masters) {
                List<Mark> marks = markDao.getMarksByMasterId(currentMaster.getId());
                UserLevel userLevel = userLevelDao.getUserLevelByUserId(currentMaster.getId());
                dtos.add(UserMasterDTO.build(currentMaster, userLevel, countRating(marks), locale));
            }
            return dtos;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private double countRating(List<Mark> marks) {
        double rating = 0;
        for (Mark mark : marks) {
            rating += mark.getMark();
        }
        return rating / marks.size();
    }

    @Override
    public int getCountOfMasters(List<MastersLevel> levels, List<Integer> serviceIds, List<Integer> categoriesIds, String search) throws ServiceException {
        try {
            return userDao.getCountOfMasters(levels, serviceIds, categoriesIds, search);
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findBySearch(String search) throws ServiceException {
        try {
            return userDao.findBySearch(search);
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateRole(int userId, String action, Role role) throws ServiceException {
        try {
            userDao.updateRole(userId, action, role);
            if (isNewHairdresser(action, role)) {
                userLevelDao.create(new UserLevel(userId, MastersLevel.YOUNG, " ", " ", true));
            } else if (isMasterRemoved(action, role)) {
                userLevelDao.removeById(userId);
            }
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }

    private boolean isMasterRemoved(String action, Role role) {
        return action.equals(REMOVE) && role == Role.HAIRDRESSER;
    }

    private boolean isNewHairdresser(String action, Role role) {
        return action.equals(ADD) && role == Role.HAIRDRESSER;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelDao(UserLevelDao userLevelDao) {
        this.userLevelDao = userLevelDao;
    }

    public void setMarkDao(MarkDao markDao) {
        this.markDao = markDao;
    }
}
