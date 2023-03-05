package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.password.BasicPasswordEncryptor;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.InvitationDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dao.UserLevelDao;
import ua.vspelykh.salon.model.dto.UserMasterDTO;
import ua.vspelykh.salon.model.entity.*;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.service.UserService;
import ua.vspelykh.salon.util.MasterFilter;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;
import ua.vspelykh.salon.util.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.controller.ControllerConstants.ADD;
import static ua.vspelykh.salon.controller.ControllerConstants.REMOVE;
import static ua.vspelykh.salon.model.dao.mapper.Column.KEY;
import static ua.vspelykh.salon.util.validation.Validation.checkUser;

/**
 * Implementation of the UserService interface that provides methods to manage user entities.
 *
 * @version 1.0
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private UserDao userDao;
    private UserLevelDao userLevelDao;
    private FeedbackDao feedbackDao;
    private InvitationDao invitationDao;

    private Transaction transaction;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the unique identifier of the user to retrieve.
     * @return the user object associated with the specified ID.
     * @throws ServiceException if there was an error retrieving the user from the data store.
     */
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

    /**
     * Retrieves a user by their email and password.
     *
     * @param email    the email address associated with the user to retrieve.
     * @param password the password associated with the user to retrieve.
     * @return the user object associated with the specified email and password.
     * @throws ServiceException if there was an error retrieving the user from the data store or if the
     *                          provided email and password combination was incorrect.
     */
    @Override
    public User findByEmailAndPassword(String email, String password) throws ServiceException {
        try {
            transaction.start();
            BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
            User user = userDao.findByEmail(email);
            if (passwordEncryptor.checkPassword(password, user.getPassword())) {
                transaction.commit();
                return user;
            } else {
                transaction.rollback();
                throw new ServiceException("Incorrect username or password");
            }
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException("Incorrect username or password", e);
        }
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all user objects stored in the data store.
     * @throws ServiceException if there was an error retrieving the list of users from the data store.
     */
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

    /**
     * Retrieves a list of all active or inactive masters.
     *
     * @param isActive a boolean value indicating whether to retrieve active masters (true) or inactive masters (false).
     * @return a list of all active or inactive master user objects stored in the data store.
     * @throws ServiceException if there was an error retrieving the list of masters from the data store.
     */
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

    /**
     * Saves a user to the data store and assigns a CLIENT role to the user.
     *
     * @param user the user object to save.
     * @throws ServiceException if there was an error validating or saving the user to the data store.
     */
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
        } catch (ValidationException | DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save user");
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Saves a user to the data store and assigns a CLIENT and invited role (HAIRDRESSER or ADMINISTRATOR) to the user.
     *
     * @param user the user object to save.
     * @param key  a secret key that must match the key associated with the user's invitation to assign the invited role.
     * @throws ServiceException if there was an error validating or saving the user to the data store, or if the key is invalid.
     */
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
        } catch (ValidationException | DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save user");
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Deletes a user with the specified ID from the data store and removes all associated roles.
     *
     * @param id the ID of the user to delete.
     * @throws ServiceException if there was an error deleting the user from the data store.
     */
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

    /**
     * Retrieves the user level associated with the specified user ID from the data store.
     *
     * @param userId the ID of the user for which to retrieve the user level.
     * @return the user level associated with the specified user ID.
     * @throws ServiceException if there was an error retrieving the user level from the data store.
     */
    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws ServiceException {
        try {
            return userLevelDao.getUserLevelByUserId(userId);
        } catch (DaoException e) {
            LOG.error("User must have a master role");
            throw new ServiceException(e);
        }
    }

    /**
     * Creates a new user level in the data store.
     *
     * @param userLevel the user level to create.
     * @throws ServiceException if there was an error creating the user level in the data store.
     */
    @Override
    public void create(UserLevel userLevel) throws ServiceException {
        try {
            userLevelDao.create(userLevel);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Updates an existing user level in the data store.
     *
     * @param userLevel the user level to update.
     * @throws ServiceException if there was an error updating the user level in the data store.
     */
    @Override
    public void update(UserLevel userLevel) throws ServiceException {
        try {
            userLevelDao.update(userLevel);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Retrieves a list of DTOs representing masters filtered by the given criteria.
     *
     * @param filter the filter to apply when searching for masters
     * @param page   the page number to retrieve (starting from 1)
     * @param size   the maximum number of items per page
     * @param sort   the sorting criteria to apply
     * @param locale the locale to use when building the DTOs
     * @return a list of UserMasterDTO objects representing the filtered masters
     * @throws ServiceException if an error occurs while retrieving the masters
     */
    @Override
    public List<UserMasterDTO> getMastersDto(MasterFilter filter, int page, int size, MasterSort sort, String locale) throws ServiceException {
        try {
            transaction.start();
            List<User> masters = userDao.findFiltered(filter, page, size, sort);
            List<UserMasterDTO> dtos = new ArrayList<>();
            for (User currentMaster : masters) {
                List<Feedback> feedbacks = feedbackDao.getFeedbacksByMasterId(currentMaster.getId());
                UserLevel userLevel = userLevelDao.getUserLevelByUserId(currentMaster.getId());
                dtos.add(UserMasterDTO.build(currentMaster, userLevel, countRating(feedbacks), locale));
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

    /**
     * Calculates the average rating based on the provided list of feedback marks.
     *
     * @param feedbacks the list of feedback marks to be used in the calculation
     * @return the calculated average rating
     */
    private double countRating(List<Feedback> feedbacks) {
        double rating = 0;
        for (Feedback feedback : feedbacks) {
            rating += feedback.getMark();
        }
        return rating / feedbacks.size();
    }

    /**
     * Returns the count of all masters that match the given filter.
     *
     * @param filter the filter to apply to the search
     * @return the count of all masters that match the given filter
     * @throws ServiceException if there was an error in the service layer
     */
    @Override
    public int getCountOfMasters(MasterFilter filter) throws ServiceException {
        try {
            return userDao.getCountOfMasters(filter);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Finds users by a given search string.
     *
     * @param search the search string to match against the users' names or emails
     * @return a list of users that match the search string
     * @throws ServiceException if there was an error performing the operation
     */
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

    /**
     * Updates the role of a user.
     *
     * @param userId the ID of the user to update
     * @param action the action to take (ADD or REMOVE)
     * @param role   the role to add or remove
     * @throws ServiceException if there is an error updating the user's role
     */
    @Override
    public void updateRole(int userId, String action, Role role) throws ServiceException {
        try {
            transaction.start();
            userDao.updateRole(userId, action, role);
            editUserLevelIfRoleIsHairdresser(action, role, userId);
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

    /**
     * Edits the user level if the role of the user is a hairdresser, based on the provided action and role.
     * If the action is to add a new hairdresser and the user does not have a user level yet, creates a new user level.
     * If the action is to add a new hairdresser and the user already has a user level, updates the user level to active.
     * If the action is to remove a hairdresser, updates the user level to inactive.
     *
     * @param action the action to be performed on the user role (ADD or REMOVE)
     * @param role   the new role of the user
     * @param userId the ID of the user whose role is being updated
     * @throws DaoException if there is an error accessing or updating the database
     */
    private void editUserLevelIfRoleIsHairdresser(String action, Role role, int userId) throws DaoException {
        if (isNewHairdresser(action, role)) {
            if (!userLevelDao.isExist(userId)) {
                userLevelDao.create(new UserLevel(userId, MastersLevel.YOUNG, "New master", "Новий майстер", true));
            } else {
                UserLevel userLevel = userLevelDao.findById(userId);
                userLevel.setActive(true);
                userLevelDao.update(userLevel);
            }
        } else if (action.equals(REMOVE) && role == Role.HAIRDRESSER) {
            UserLevel level = userLevelDao.findById(userId);
            level.setActive(false);
            userLevelDao.update(level);
        }
    }

    private boolean isNewHairdresser(String action, Role role) {
        return (action.equals(ADD) && role == Role.HAIRDRESSER);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserLevelDao(UserLevelDao userLevelDao) {
        this.userLevelDao = userLevelDao;
    }

    public void setFeedbackDao(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setInvitationDao(InvitationDao invitationDao) {
        this.invitationDao = invitationDao;
    }
}
