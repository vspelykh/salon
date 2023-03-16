package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

/**
 * This interface extends the Dao interface for the BaseService entity type,adding methods to retrieve
 * a list of base services by categories and get the count of categories.
 *
 * @version 1.0
 */
public interface BaseServiceDao extends Dao<BaseService> {

    /**
     * Returns a list of base services that belong to the specified categories.
     *
     * @param categoriesIds the IDs of the categories to filter by
     * @param page          the page number to retrieve (starting from 0)
     * @param size          the maximum number of items to retrieve
     * @return a list of base services that belong to the specified categories
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<BaseService> findByFilter(List<Integer> categoriesIds, int page, int size) throws DaoException;

    /**
     * Returns the count of base services that belong to the specified categories.
     *
     * @param categoriesIds the IDs of the categories to filter by
     * @return the count of base services that belong to the specified categories
     * @throws DaoException if an error occurs while accessing the data source
     */
    int getCountOfCategories(List<Integer> categoriesIds) throws DaoException;
}
