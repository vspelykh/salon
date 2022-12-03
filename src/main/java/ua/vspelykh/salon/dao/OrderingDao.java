package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface OrderingDao extends Dao<Ordering>{

    List<Ordering> getByAppointmentId(Integer appointmentId) throws DaoException;
}
