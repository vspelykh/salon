package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface AppointmentItemDao extends Dao<AppointmentItem>{

    List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws DaoException;
}
