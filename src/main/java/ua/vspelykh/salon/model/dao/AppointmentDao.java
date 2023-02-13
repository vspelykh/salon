package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;
import ua.vspelykh.salon.util.exception.DaoException;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentDao extends Dao<Appointment> {

    List<Appointment> getByMasterId(Integer masterId) throws DaoException;

    List<Appointment> getByClientId(Integer clientId) throws DaoException;

    List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws DaoException;

    List<Appointment> getByDateAndClientId(LocalDate date, int clientId) throws DaoException;

    List<Appointment> getAllByDate(LocalDate date) throws DaoException;

    List<Appointment> getFiltered(Integer masterId, LocalDate dateFrom, LocalDate dateTo,
                                  AppointmentStatus status, PaymentStatus paymentStatus, int page, int size) throws DaoException;

    int getCountOfAppointments(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus) throws DaoException;
}
