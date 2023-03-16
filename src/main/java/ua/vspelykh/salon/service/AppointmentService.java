package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.AppointmentDto;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.AppointmentFilter;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * This interface defines the methods to manage appointments.
 *
 * @version 1.0
 */
public interface AppointmentService {

    /**
     * Retrieves the appointment with the specified ID.
     *
     * @param id the ID of the appointment to retrieve
     * @return the appointment with the specified ID
     * @throws ServiceException if an error occurs while retrieving the appointment
     */
    Appointment findById(Integer id) throws ServiceException;

    /**
     * Saves the specified appointment.
     *
     * @param appointment the appointment to be saved
     * @throws ServiceException if an error occurs while saving the appointment
     */
    void save(Appointment appointment) throws ServiceException;

    /**
     * Saves the specified appointment with the specified list of master services.
     *
     * @param appointment    the appointment to be saved
     * @param masterServices the list of master services to be associated with the appointment
     * @throws ServiceException if an error occurs while saving the appointment or its associated master services
     */
    void save(Appointment appointment, List<MasterService> masterServices) throws ServiceException;

    /**
     * Deletes the appointment with the specified ID.
     *
     * @param id the ID of the appointment to be deleted
     * @throws ServiceException if an error occurs while deleting the appointment
     */
    void delete(Integer id) throws ServiceException;

    /**
     * Retrieves a list of appointments that belong to the specified date and master ID.
     *
     * @param date     the date for which to retrieve the appointments
     * @param masterId the ID of the master for which to retrieve the appointments
     * @return a list of appointments that belong to the specified date and master ID
     * @throws ServiceException if an error occurs while retrieving the appointments
     */
    List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws ServiceException;

    /**
     * Retrieves a list of appointment DTOs that belong to the specified date and master ID.
     *
     * @param date     the date for which to retrieve the appointment DTOs
     * @param masterId the ID of the master for which to retrieve the appointment DTOs
     * @return a list of appointment DTOs that belong to the specified date and master ID
     * @throws ServiceException if an error occurs while retrieving the appointment DTOs
     */
    List<AppointmentDto> getDTOsByDateAndMasterId(LocalDate date, int masterId) throws ServiceException;

    /**
     * Retrieves a list of appointment DTOs that belong to the specified date.
     *
     * @param date the date for which to retrieve the appointment DTOs
     * @return a list of appointment DTOs that belong to the specified date
     * @throws ServiceException if an error occurs while retrieving the appointment DTOs
     */
    List<AppointmentDto> getAllByDate(LocalDate date) throws ServiceException;

    /**
     * Retrieves a list of appointment DTOs that match the specified filter criteria.
     *
     * @param filter the filter criteria to apply
     * @param page   the page number of the results to retrieve
     * @param size   the number of results to retrieve per page
     * @return a list of appointment DTOs that match the specified filter criteria
     * @throws ServiceException if an error occurs while retrieving the appointment DTOs
     */
    List<AppointmentDto> getFiltered(AppointmentFilter filter, int page, int size) throws ServiceException;

    /**
     * Retrieves the count of appointments that match the specified filter criteria.
     *
     * @param filter the filter criteria to apply
     * @return the count of appointments that match the specified filter criteria
     * @throws ServiceException if an error occurs while retrieving the count of appointments
     */
    int getCountOfAppointments(AppointmentFilter filter) throws ServiceException;
}
