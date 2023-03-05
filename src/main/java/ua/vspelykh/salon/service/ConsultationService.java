package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage consultations.
 *
 * @version 1.0
 */
public interface ConsultationService {

    /**
     * Retrieves a list of all consultations.
     *
     * @return a list of all consultations
     * @throws ServiceException if an error occurs while retrieving the consultations
     */
    List<Consultation> findAll() throws ServiceException;

    /**
     * Saves the specified consultation.
     *
     * @param consultation the consultation to be saved
     * @throws ServiceException if an error occurs while saving the consultation
     */
    void save(Consultation consultation) throws ServiceException;

    /**
     * Deletes the consultation with the specified ID.
     *
     * @param id the ID of the consultation to be deleted
     * @throws ServiceException if an error occurs while deleting the consultation
     */
    void delete(Integer id) throws ServiceException;
}
