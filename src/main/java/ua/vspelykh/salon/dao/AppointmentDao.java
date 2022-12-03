package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.util.exception.DaoException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentDao extends Dao<Appointment> {

    List<Appointment> getByMasterId(Integer masterId) throws DaoException;

    List<Appointment> getByClientId(Integer clientId) throws DaoException;

    List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws DaoException;

    List<Appointment> getByDateAndClientId(LocalDate date, int clientId) throws DaoException;

}