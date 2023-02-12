package ua.vspelykh.salon.service;

import ua.vspelykh.salon.dto.FeedbackDto;
import ua.vspelykh.salon.model.Feedback;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface MarkService {

    void save(Feedback mark) throws ServiceException;

    List<FeedbackDto> getMarksByMasterId(Integer masterId, int page) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    int countMarksByMasterId(Integer masterID) throws ServiceException;

    Feedback getMarkByAppointmentId(Integer appointmentId);
}
