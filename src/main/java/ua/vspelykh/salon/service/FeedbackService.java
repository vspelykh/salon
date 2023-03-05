package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.FeedbackDto;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage feedback.
 *
 * @version 1.0
 */
public interface FeedbackService {

    /**
     * Saves the specified feedback.
     *
     * @param feedback the feedback to be saved
     * @throws ServiceException if an error occurs while saving the feedback
     */
    void save(Feedback feedback) throws ServiceException;

    /**
     * Retrieves a list of feedback DTOs for the specified master ID and page number.
     *
     * @param masterId the ID of the master for which to retrieve the feedback DTOs
     * @param page     the page number of the results to retrieve
     * @return a list of feedback DTOs for the specified master ID and page number
     * @throws ServiceException if an error occurs while retrieving the feedback DTOs
     */
    List<FeedbackDto> getByMasterId(Integer masterId, int page) throws ServiceException;

    /**
     * Deletes the feedback with the specified ID.
     *
     * @param id the ID of the feedback to be deleted
     * @throws ServiceException if an error occurs while deleting the feedback
     */
    void delete(Integer id) throws ServiceException;

    /**
     * Retrieves the count of feedbacks for the specified master ID.
     *
     * @param masterID the ID of the master for which to retrieve the count of feedbacks
     * @return the count of feedbacks for the specified master ID
     * @throws ServiceException if an error occurs while retrieving the count of feedbacks
     */
    int countByMasterId(Integer masterID) throws ServiceException;

    /**
     * Retrieves the feedback for the specified appointment ID.
     *
     * @param appointmentId the ID of the appointment for which to retrieve the feedback
     * @return the feedback for the specified appointment ID
     */
    Feedback getByAppointmentId(Integer appointmentId);
}