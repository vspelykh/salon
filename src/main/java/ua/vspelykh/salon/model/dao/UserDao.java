package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Role;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.util.MasterFilter;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

/**
 * The interface UserDao describes the functionality to access and manipulate the user data in the database.
 *
 * @version 1.0
 */
public interface UserDao extends Dao<User> {

    /**
     * Finds a user by email.
     *
     * @param email the email to search for
     * @return the User object if found, null otherwise
     * @throws DaoException if an error occurred while accessing the database
     */
    User findByEmail(String email) throws DaoException;

    /**
     * Returns a list of all clients in the database.
     *
     * @return the list of all clients
     * @throws DaoException if an error occurred while accessing the database
     */
    List<User> findClients() throws DaoException;

    /**
     * Returns a list of all masters in the database.
     *
     * @return the list of all masters
     * @throws DaoException if an error occurred while accessing the database
     */
    List<User> findMasters() throws DaoException;

    /**
     * Finds and returns a filtered list of masters.
     *
     * @param filter the filter object containing the search criteria
     * @param page   the page number
     * @param size   the number of items per page
     * @param sort   the sorting criteria
     * @return the filtered list of masters
     * @throws DaoException if an error occurred while accessing the database
     */
    List<User> findFiltered(MasterFilter filter, int page, int size, MasterSort sort) throws DaoException;

    /**
     * Returns the count of masters that match the given filter.
     *
     * @param filter the filter object containing the search criteria
     * @return the count of masters that match the given filter
     * @throws DaoException if an error occurred while accessing the database
     */
    int getCountOfMasters(MasterFilter filter) throws DaoException;

    /**
     * Finds users by search query.
     *
     * @param search the search query
     * @return the list of users matching the search query
     * @throws DaoException if an error occurred while accessing the database
     */
    List<User> findBySearch(String search) throws DaoException;

    /**
     * Updates the role of a user.
     *
     * @param userId the ID of the user to update
     * @param action the action to perform (add or remove)
     * @param role   the role to add or remove
     * @throws DaoException if an error occurred while accessing the database
     */
    void updateRole(int userId, String action, Role role) throws DaoException;
}
