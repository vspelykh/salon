package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.util.List;

/**
 * This interface defines CRUD (Create, Read, Update, Delete) operations for a generic entity type T.
 *
 * @version 1.0
 */
public interface Dao<T> {

    /**
     * Returns the entity with the specified ID.
     *
     * @param id the ID of the entity to find
     * @return the entity with the specified ID, or null if not found
     * @throws DaoException if an error occurs while accessing the data source
     */
    T findById(int id) throws DaoException;

    /**
     * Returns a list of all entities.
     *
     * @return a list of all entities
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<T> findAll() throws DaoException;

    /**
     * Creates a new entity.
     *
     * @param entity the entity to create
     * @return the ID of the newly created entity
     * @throws DaoException if an error occurs while accessing the data source
     */
    int create(T entity) throws DaoException;

    /**
     * Updates an existing entity.
     *
     * @param entity the entity to update
     * @throws DaoException if an error occurs while accessing the data source
     */
    void update(T entity) throws DaoException;

    /**
     * Deletes the entity with the specified ID.
     *
     * @param id the ID of the entity to delete
     * @throws DaoException if an error occurs while accessing the data source
     */
    void removeById(int id) throws DaoException;

    /**
     * Sets the database connection to be used by this DAO.
     *
     * @param connection the database connection
     */
    void setConnection(Connection connection);
}
