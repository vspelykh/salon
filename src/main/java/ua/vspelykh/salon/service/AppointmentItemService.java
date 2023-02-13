package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface AppointmentItemService {

    void save(AppointmentItem appointmentItem) throws ServiceException;

    List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws ServiceException;
}
