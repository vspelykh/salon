package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.Feedback;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface FeedbackDao extends Dao<Feedback>{

    List<Feedback> getFeedbacksByMasterId(Integer masterId, int page) throws DaoException;

    int countFeedbacksByMasterId(Integer masterId) throws DaoException;

    Feedback findByAppointmentId(Integer appointmentId) throws DaoException;
}
