package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.UserMasterDTO;
import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.MasterFilter;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage users.
 *
 * @version 1.0
 */
public interface UserService {

    /**
     * Retrieves the user with the specified ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID
     * @throws ServiceException if an error occurs while retrieving the user
     */
    User findById(Integer id) throws ServiceException;

    /**
     * Retrieves the user with the specified email and password.
     *
     * @param email    the email of the user to retrieve
     * @param password the password of the user to retrieve
     * @return the user with the specified email and password
     * @throws ServiceException if an error occurs while retrieving the user
     */
    User findByEmailAndPassword(String email, String password) throws ServiceException;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     * @throws ServiceException if an error occurs while retrieving the users
     */
    List<User> findAll() throws ServiceException;

    /**
     * Retrieves a list of masters based on the specified isActive flag.
     *
     * @param isActive a flag indicating whether to retrieve active or inactive masters
     * @return a list of masters based on the specified isActive flag
     * @throws ServiceException if an error occurs while retrieving the masters
     */
    List<User> findMasters(boolean isActive) throws ServiceException;

    /**
     * Saves the specified user.
     *
     * @param user the user to save
     * @throws ServiceException if an error occurs while saving the user
     */
    void save(User user) throws ServiceException;

    /**
     * Saves the specified user with the specified parameter.
     *
     * @param user      the user to save
     * @param parameter the parameter to save with the user
     * @throws ServiceException if an error occurs while saving the user
     */
    void save(User user, String parameter) throws ServiceException;

    /**
     * Deletes the user with the specified ID.
     *
     * @param id the ID of the user to delete
     * @throws ServiceException if an error occurs while deleting the user
     */
    void delete(Integer id) throws ServiceException;

    /**
     * Retrieves the user level for the specified user ID.
     *
     * @param userId the ID of the user for which to retrieve the user level
     * @return the user level for the specified user ID
     * @throws ServiceException if an error occurs while retrieving the user level
     */
    UserLevel getUserLevelByUserId(Integer userId) throws ServiceException;

    /**
     * Creates the specified user level.
     *
     * @param userLevel the user level to create
     * @throws ServiceException if an error occurs while creating the user level
     */
    void create(UserLevel userLevel) throws ServiceException;

    /**
     * Updates the specified user level.
     *
     * @param userLevel the user level to update
     * @throws ServiceException if an error occurs while updating the user level
     */
    void update(UserLevel userLevel) throws ServiceException;

    /**
     * Retrieves a list of user master DTOs based on the provided filter, page, size, sorting, and locale.
     *
     * @param filter the filter to apply to the list of user master DTOs
     * @param page   the page number of the list of user master DTOs to retrieve
     * @param size   the number of user master DTOs to retrieve per page
     * @param sort   the sorting to apply to the list of user master DTOs
     * @param locale the locale to use for the list of user master DTOs
     * @return a list of user master DTOs based on the provided filter, page, size, sorting, and locale
     * @throws ServiceException if an error occurs while retrieving the user master DTOs
     */
    List<UserMasterDTO> getMastersDto(MasterFilter filter, int page, int size, MasterSort sort, String locale) throws ServiceException;

    /**
     * Retrieves the count of user master DTOs based on the provided filter.
     *
     * @param filter the filter to apply to the count of user master DTOs
     * @return the count of user master DTOs based on the provided filter
     * @throws ServiceException if an error occurs while retrieving the count of user master DTOs
     */
    int getCountOfMasters(MasterFilter filter) throws ServiceException;

    /**
     * Finds a list of users based on the specified search string.
     *
     * @param search the search string to use when searching for users by email or number.
     * @return a list of users matching the specified search string
     * @throws ServiceException if an error occurs while retrieving the list of users
     */
    List<User> findBySearch(String search) throws ServiceException;

    /**
     * Updates the role of a user based on the specified user ID, action, and role.
     *
     * @param userId the ID of the user whose role to update
     * @param action the action to perform on the user's role (e.g. "add" or "remove")
     * @param role   the role to update the user to
     * @throws ServiceException if an error occurs while updating the user's role
     */
    void updateRole(int userId, String action, Role role) throws ServiceException;
}