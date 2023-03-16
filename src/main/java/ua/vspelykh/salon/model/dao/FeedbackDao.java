package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

/**
 * This interface extends the Dao interface for the Feedback entity type, adding methods to retrieve
 * feedbacks by master ID and appointment ID, and to count feedbacks by master ID.
 *
 * @version 1.0
 */
public interface FeedbackDao extends Dao<Feedback> {

    /**
     * Returns a list of feedbacks for the specified master ID.
     *
     * @param masterId the ID of the master to retrieve feedbacks for
     * @return a list of feedbacks for the specified master ID
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<Feedback> getFeedbacksByMasterId(Integer masterId) throws DaoException;

    /**
     * Returns a list of feedbacks for the specified master ID and page number.
     *
     * @param masterId the ID of the master to retrieve feedbacks for
     * @param page     the page number to retrieve (starting from 0)
     * @return a list of feedbacks for the specified master ID and page number
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<Feedback> getFeedbacksByMasterId(Integer masterId, int page) throws DaoException;

    /**
     * Returns the count of feedbacks for the specified master ID.
     *
     * @param masterId the ID of the master to count feedbacks for
     * @return the count of feedbacks for the specified master ID
     * @throws DaoException if an error occurs while accessing the data source
     */
    int countByMasterId(Integer masterId) throws DaoException;

    /**
     * Returns the feedback with the specified appointment ID.
     *
     * @param appointmentId the ID of the appointment to retrieve feedback for
     * @return the feedback with the specified appointment ID
     * @throws DaoException if an error occurs while accessing the data source
     */
    Feedback findByAppointmentId(Integer appointmentId) throws DaoException;
}
