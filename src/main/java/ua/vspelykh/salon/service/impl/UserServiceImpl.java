package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.dao.InvitationDao;
import ua.vspelykh.salon.dao.FeedbackDao;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.UserLevelDao;
import ua.vspelykh.salon.dto.UserMasterDTO;
import ua.vspelykh.salon.model.*;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.command.user.ChangeRoleCommand.ADD;
import static ua.vspelykh.salon.controller.command.user.ChangeRoleCommand.REMOVE;
import static ua.vspelykh.salon.dao.mapper.Column.KEY;
import static ua.vspelykh.salon.util.validation.Validation.checkUser;

public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private UserLevelDao userLevelDao;
    private FeedbackDao feedbackDao;
    private InvitationDao invitationDao;

    private Transaction transaction;

    @Override
    public User findById(Integer id) throws ServiceException {
        try {
            transaction.start();
            User user = userDao.findById(id);
            transaction.commit();
            return user;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error(String.format("Can't find a user with id %d", id));
            throw new ServiceException(e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws ServiceException {
        try {
            transaction.start();
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            User user = userDao.findByEmail(email);
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                transaction.commit();
                return user;
            } else throw new ServiceException("Incorrect username or password");
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException("Incorrect username or password", e);
        }
    }

    @Override
    public User findByNumber(String number) throws ServiceException {
        try {
            transaction.start();
            User user = userDao.findByNumber(number);
            transaction.commit();
            return user;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error(String.format("User with number %s didn't find", number));
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            transaction.start();
            List<User> users = userDao.findAll();
            transaction.commit();
            return users;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findClients() throws ServiceException {
        try {
            transaction.start();
            List<User> clients = userDao.findClients();
            transaction.commit();
            return clients;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findMasters(boolean isActive) throws ServiceException {
        try {
            transaction.start();
            List<User> masters = userDao.findMasters();
            transaction.commit();
            return masters;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public List<User> findAdministrators() throws ServiceException {
        try {
            transaction.start();
            List<User> administrators = userDao.findAdministrators();
            transaction.commit();
            return administrators;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(User user) throws ServiceException {
        try {
            transaction.start();
            checkUser(user);
            if (user.isNew()) {
                userDao.create(user);
                user = userDao.findByEmail(user.getEmail());
                userDao.updateRole(user.getId(), ADD, Role.CLIENT);
            } else {
                userDao.update(user);
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save user");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(User user, String key) throws ServiceException {
        try {
            transaction.start();
            checkUser(user);
            userDao.create(user);
            user = userDao.findByEmail(user.getEmail());
            Invitation invitation = invitationDao.findByEmail(user.getEmail());
            BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
            if (encryptor.checkPassword(key, invitation.getKey())) {
                userDao.updateRole(user.getId(), ADD, Role.CLIENT);
                userDao.updateRole(user.getId(), ADD, invitation.getRole());
            } else {
                transaction.rollback();
                throw new ServiceException(KEY);
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save user");
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            transaction.start();
            User user = userDao.findById(id);
            userDao.removeById(id);
            for (Role role : user.getRoles()) {
                userDao.updateRole(id, REMOVE, role);
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
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
            transaction.start();
            List<UserMasterDTO> dtos = new ArrayList<>();
            List<User> masters = userDao.findMastersByLevelsAndServices(levels, serviceIds, categoriesIds, search, page, size, sort);

            for (User currentMaster : masters) {
                List<Feedback> marks = feedbackDao.getFeedbacksByMasterId(currentMaster.getId(), page);
                UserLevel userLevel = userLevelDao.getUserLevelByUserId(currentMaster.getId());
                dtos.add(UserMasterDTO.build(currentMaster, userLevel, countRating(marks), locale));
            }
            transaction.commit();
            return dtos;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    private double countRating(List<Feedback> marks) {
        double rating = 0;
        for (Feedback mark : marks) {
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
            transaction.start();
            List<User> users = userDao.findBySearch(search);
            transaction.commit();
            return users;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateRole(int userId, String action, Role role) throws ServiceException {
        try {
            transaction.start();
            userDao.updateRole(userId, action, role);
            if (isNewHairdresser(action, role)) {
                userLevelDao.create(new UserLevel(userId, MastersLevel.YOUNG, "New master", "Новий майстер", true));
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
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

    public void setMarkDao(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setInvitationDao(InvitationDao invitationDao) {
        this.invitationDao = invitationDao;
    }
}
