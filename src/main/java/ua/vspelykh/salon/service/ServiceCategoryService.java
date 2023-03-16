package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.ServiceCategoryDto;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage service categories.
 *
 * @version 1.0
 */
public interface ServiceCategoryService {

    /**
     * Retrieves the service category with the specified ID.
     *
     * @param id the ID of the service category to retrieve
     * @return the service category with the specified ID
     * @throws ServiceException if an error occurs while retrieving the service category
     */
    ServiceCategory findById(int id) throws ServiceException;

    /**
     * Retrieves a list of service category DTOs for the specified locale.
     *
     * @param locale the locale in which to retrieve the service category DTOs
     * @return a list of service category DTOs for the specified locale
     * @throws ServiceException if an error occurs while retrieving the service category DTOs
     */
    List<ServiceCategoryDto> findAll(String locale) throws ServiceException;
}




