package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dto.FeedbackDto;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.FeedbackService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implementation of the FeedbackService interface, providing methods to manage feedback
 * related operations such as saving, deleting, getting feedback by appointment ID and counting feedback by master ID.
 *
 * @version 1.0
 */
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger LOG = LogManager.getLogger(FeedbackServiceImpl.class);

    private FeedbackDao feedbackDao;
    private AppointmentDao appointmentDao;
    private UserDao userDao;
    private Transaction transaction;

    /**
     * Saves the new feedback.
     *
     * @param feedback the feedback to save
     * @throws ServiceException if there was an error saving the feedback
     */
    @Override
    public void save(Feedback feedback) throws ServiceException {
        try {
            if (!feedback.isNew()) {
                throw new ServiceException("Edit feedback is forbidden operation!");
            }
            transaction.start();
            feedbackDao.create(feedback);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save feedback");
            throw new ServiceException(e);
        }
    }

    /**
     * Gets the list of feedbacks by the given master ID and page number.
     *
     * @param masterId the ID of the master to get feedbacks for
     * @param page     the page number to retrieve
     * @return a list of FeedbackDto objects
     * @throws ServiceException if there was an error getting the feedbacks
     */
    @Override
    public List<FeedbackDto> getByMasterId(Integer masterId, int page) throws ServiceException {
        try {
            transaction.start();
            List<Feedback> feedbacks = feedbackDao.getFeedbacksByMasterId(masterId, page);
            transaction.commit();
            return toDTOs(feedbacks);
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to get marks for master");
            throw new ServiceException(e);
        }
    }

    /**
     * Converts a list of Feedback objects to a list of FeedbackDto objects.
     *
     * @param feedbacks the list of Feedback objects to convert
     * @return a list of FeedbackDto objects
     * @throws DaoException if there was an error accessing the data layer
     */
    private List<FeedbackDto> toDTOs(List<Feedback> feedbacks) throws DaoException {
        List<FeedbackDto> dtos = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            dtos.add(toDTO(feedback));
        }
        return dtos;
    }

    /**
     * Converts a Feedback object to a FeedbackDto object.
     *
     * @param feedback the Feedback object to convert
     * @return a FeedbackDto object
     * @throws DaoException if there was an error accessing the data layer
     */
    private FeedbackDto toDTO(Feedback feedback) throws DaoException {
        User client = userDao.findById(appointmentDao.findById(feedback.getAppointmentId()).getClientId());
        return new FeedbackDto.FeedbackDtoBuilder(client, feedback).build();
    }

    /**
     * Deletes the feedback with the given ID.
     *
     * @param id the ID of the feedback to delete
     * @throws ServiceException if there was an error deleting the feedback
     */
    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            transaction.start();
            feedbackDao.removeById(id);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to delete feedback");
            throw new ServiceException(e);
        }
    }

    /**
     * Counts the number of feedbacks for the given master ID.
     *
     * @param masterID the ID of the master to count feedbacks for
     * @return the number of feedbacks for the given master ID
     * @throws ServiceException if there was an error counting the feedbacks
     */
    @Override
    public int countByMasterId(Integer masterID) throws ServiceException {
        try {
            return feedbackDao.countByMasterId(masterID);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Gets the feedback for the given appointment ID.
     *
     * @param appointmentId the ID of the appointment to get feedback for
     * @return the Feedback object for the given appointment ID, or null if it doesn't exist
     */
    @Override
    public Feedback getByAppointmentId(Integer appointmentId) {
        try {
            return feedbackDao.findByAppointmentId(appointmentId);
        } catch (DaoException e) {
            return null;
        }
    }

    public void setFeedbackDao(FeedbackDao feedbackDao) {
        this.feedbackDao = feedbackDao;
    }

    public void setAppointmentDao(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}