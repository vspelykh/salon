package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface OrderingService {

    void save(Ordering ordering) throws ServiceException;

    List<Ordering> getByAppointmentId(Integer appointmentId) throws ServiceException;
}
