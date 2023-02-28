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

public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger LOG = LogManager.getLogger(FeedbackServiceImpl.class);

    private FeedbackDao feedbackDao;
    private AppointmentDao appointmentDao;
    private UserDao userDao;
    private Transaction transaction;

    @Override
    public void save(Feedback feedback) throws ServiceException {
        try {
            if (!feedback.isNew()){
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

    @Override
    public List<FeedbackDto> getFeedbacksByMasterId(Integer masterId, int page) throws ServiceException {
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

    private List<FeedbackDto> toDTOs(List<Feedback> feedbacks) throws DaoException {
        List<FeedbackDto> dtos = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            dtos.add(toDTO(feedback));
        }
        return dtos;
    }

    private FeedbackDto toDTO(Feedback feedback) throws DaoException {
        User client = userDao.findById(appointmentDao.findById(feedback.getAppointmentId()).getClientId());
        return new FeedbackDto.FeedbackDtoBuilder(client, feedback).build();
    }

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

    @Override
    public int countFeedbacksByMasterId(Integer masterID) throws ServiceException {
        try {
            return feedbackDao.countFeedbacksByMasterId(masterID);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Feedback getFeedbackByAppointmentId(Integer appointmentId) {
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