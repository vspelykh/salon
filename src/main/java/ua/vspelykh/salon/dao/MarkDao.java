package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface MarkDao extends Dao<Mark>{

    List<Mark> getMarksByMasterId(Integer masterId, int page) throws DaoException;

    int countMarksByMasterId(Integer masterId) throws DaoException;

    Mark findByAppointmentId(Integer appointmentId) throws DaoException;
}
