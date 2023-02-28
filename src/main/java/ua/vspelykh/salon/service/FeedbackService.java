package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.FeedbackDto;
import ua.vspelykh.salon.model.entity.Feedback;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface FeedbackService {

    void save(Feedback feedback) throws ServiceException;

    List<FeedbackDto> getFeedbacksByMasterId(Integer masterId, int page) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    int countFeedbacksByMasterId(Integer masterID) throws ServiceException;

    Feedback getFeedbackByAppointmentId(Integer appointmentId);
}
