package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment findById(Integer id) throws ServiceException;

    void save(Appointment appointment) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    List<Appointment> getByMasterId(Integer masterId) throws ServiceException;

    List<Appointment> getByClientId(Integer clientId) throws ServiceException;

    List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws ServiceException;

}
