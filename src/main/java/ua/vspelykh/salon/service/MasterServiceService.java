package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.MasterServiceDto;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage master services.
 *
 * @version 1.0
 */
public interface MasterServiceService {

    /**
     * Retrieves the master service with the specified ID.
     *
     * @param id the ID of the master service to retrieve
     * @return the master service with the specified ID
     * @throws ServiceException if an error occurs while retrieving the master service
     */
    MasterService findById(Integer id) throws ServiceException;

    /**
     * Saves the specified master service.
     *
     * @param masterService the master service to be saved
     * @throws ServiceException if an error occurs while saving the master service
     */
    void save(MasterService masterService) throws ServiceException;

    /**
     * Deletes the master service with the specified ID.
     *
     * @param id the ID of the master service to be deleted
     * @throws ServiceException if an error occurs while deleting the master service
     */
    void delete(Integer id) throws ServiceException;

    /**
     * Retrieves a list of master service DTOs for the specified master ID and locale.
     *
     * @param masterId the ID of the master for which to retrieve the master service DTOs
     * @param locale   the locale in which to retrieve the master service DTOs
     * @return a list of master service DTOs for the specified master ID and locale
     * @throws ServiceException if an error occurs while retrieving the master service DTOs
     */
    List<MasterServiceDto> getDTOsByMasterId(int masterId, String locale) throws ServiceException;
}