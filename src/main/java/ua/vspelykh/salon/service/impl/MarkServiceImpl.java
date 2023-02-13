package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AppointmentDao;
import ua.vspelykh.salon.model.dao.FeedbackDao;
import ua.vspelykh.salon.model.dao.UserDao;
import ua.vspelykh.salon.model.dto.FeedbackDto;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.service.MarkService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

public class MarkServiceImpl implements MarkService {

    private static final Logger LOG = LogManager.getLogger(MarkServiceImpl.class);

    private FeedbackDao feedbackDao;
    private AppointmentDao appointmentDao;
    private UserDao userDao;
    private Transaction transaction;

    @Override
    public void save(Feedback mark) throws ServiceException {
        try {
            transaction.start();
            feedbackDao.create(mark);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save mark");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<FeedbackDto> getMarksByMasterId(Integer masterId, int page) throws ServiceException {
        try {
            transaction.start();
            List<Feedback> marks = feedbackDao.getFeedbacksByMasterId(masterId, page);
            transaction.commit();
            return toDTOs(marks);
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

    private List<FeedbackDto> toDTOs(List<Feedback> marks) throws DaoException {
        List<FeedbackDto> dtos = new ArrayList<>();
        for (Feedback mark : marks) {
            dtos.add(toDTO(mark));
        }
        return dtos;
    }

    private FeedbackDto toDTO(Feedback mark) throws DaoException {
        User client = userDao.findById(appointmentDao.findById(mark.getAppointmentId()).getClientId());
        return new FeedbackDto.FeedbackDtoBuilder(client, mark).build();
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
            LOG.error("Error to delete mark");
            throw new ServiceException(e);
        }
    }

    @Override
    public int countMarksByMasterId(Integer masterID) throws ServiceException {
        try {
            return feedbackDao.countFeedbacksByMasterId(masterID);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Feedback getMarkByAppointmentId(Integer appointmentId) {
        try {
            return feedbackDao.findByAppointmentId(appointmentId);
        } catch (DaoException e) {
            return null;
        }
    }

    public void setMarkDao(FeedbackDao feedbackDao) {
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