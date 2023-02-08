package ua.vspelykh.salon.service;

import ua.vspelykh.salon.dto.AppointmentDto;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.AppointmentStatus;
import ua.vspelykh.salon.model.PaymentStatus;
import ua.vspelykh.salon.model.Service;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment findById(Integer id) throws ServiceException;

    void save(Appointment appointment) throws ServiceException;

    void save(Appointment appointment, List<Service> services) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    List<Appointment> getByMasterId(Integer masterId) throws ServiceException;

    List<Appointment> getByClientId(Integer clientId) throws ServiceException;

    List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws ServiceException;

    List<AppointmentDto> getDtosByDateAndMasterId(LocalDate date, int masterId) throws ServiceException;

    List<AppointmentDto> getAllByDate(LocalDate date) throws ServiceException;

    List<AppointmentDto> getFiltered(Integer masterId, LocalDate dateFrom, LocalDate dateTo,
                                     AppointmentStatus status, PaymentStatus paymentStatus, int page, int size) throws ServiceException;

    int getCountOfAppointments(Integer masterId, LocalDate dateFrom, LocalDate dateTo, AppointmentStatus status, PaymentStatus paymentStatus) throws ServiceException;

}
