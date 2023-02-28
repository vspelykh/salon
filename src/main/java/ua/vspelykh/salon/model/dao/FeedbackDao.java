package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface FeedbackDao extends Dao<Feedback>{

    List<Feedback> getFeedbacksByMasterId(Integer masterId) throws DaoException;

    List<Feedback> getFeedbacksByMasterId(Integer masterId, int page) throws DaoException;

    int countFeedbacksByMasterId(Integer masterId) throws DaoException;

    Feedback findByAppointmentId(Integer appointmentId) throws DaoException;
}
