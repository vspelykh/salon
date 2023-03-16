package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.BaseServiceDto;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage base services.
 *
 * @version 1.0
 */
public interface BaseServiceService {

    /**
     * Retrieves the base service with the specified ID.
     *
     * @param id the ID of the base service to retrieve
     * @return the base service with the specified ID
     * @throws ServiceException if an error occurs while retrieving the base service
     */
    BaseService findById(Integer id) throws ServiceException;

    /**
     * Retrieves a list of base service DTOs.
     *
     * @param locale the locale to use for retrieving the base service DTOs
     * @return a list of base service DTOs
     * @throws ServiceException if an error occurs while retrieving the base service DTOs
     */
    List<BaseServiceDto> findAll(String locale) throws ServiceException;

    /**
     * Saves the specified base service.
     *
     * @param baseService the base service to be saved
     * @throws ServiceException if an error occurs while saving the base service
     */
    void save(BaseService baseService) throws ServiceException;

    /**
     * Deletes the base service with the specified ID.
     *
     * @param baseServiceId the ID of the base service to be deleted
     * @throws ServiceException if an error occurs while deleting the base service
     */
    void delete(Integer baseServiceId) throws ServiceException;

    /**
     * Retrieves a list of base service DTOs that belong to the specified list of category IDs.
     *
     * @param categoriesIds the list of category IDs for which to retrieve the base service DTOs
     * @param page          the page number of the results to retrieve
     * @param size          the number of results to retrieve per page
     * @param locale        the locale to use for retrieving the base service DTOs
     * @return a list of base service DTOs that belong to the specified list of category IDs
     * @throws ServiceException if an error occurs while retrieving the base service DTOs
     */
    List<BaseServiceDto> findByFilter(List<Integer> categoriesIds, int page, int size, String locale) throws ServiceException;

    /**
     * Retrieves the count of categories that match the specified list of category IDs.
     *
     * @param categoriesIds the list of category IDs for which to retrieve the count
     * @param page          the page number of the results to retrieve
     * @param size          the number of results to retrieve per page
     * @return the count of categories that match the specified list of category IDs
     * @throws ServiceException if an error occurs while retrieving the count of categories
     */
    int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws ServiceException;
}
