package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.util.AppointmentFilter;
import ua.vspelykh.salon.util.exception.DaoException;

import java.time.LocalDate;
import java.util.List;

/**
 * This interface defines methods for accessing appointment data from a data source.
 * It extends the generic Dao interface with the Appointment type.
 *
 * @version 1.0
 */
public interface AppointmentDao extends Dao<Appointment> {

    /**
     * Returns a list of appointments for the specified date and master ID.
     *
     * @param date     the date to filter by
     * @param masterId the ID of the master to filter by
     * @return a list of appointments for the specified date and master ID
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<Appointment> getByDateAndMasterId(LocalDate date, int masterId) throws DaoException;

    /**
     * Returns a list of appointments for the specified date.
     *
     * @param date the date to filter by
     * @return a list of appointments for the specified date
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<Appointment> getAllByDate(LocalDate date) throws DaoException;

    /**
     * Returns a paginated list of appointments that match the specified filter.
     *
     * @param filter the AppointmentFilter object containing the filter parameters to apply
     * @param page   the page number to return
     * @param size   the number of appointments to return per page
     * @return a paginated list of appointments that match the specified filter
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<Appointment> getFiltered(AppointmentFilter filter, int page, int size) throws DaoException;

    /**
     * Returns the total count of appointments that match the specified filter.
     *
     * @param filter the AppointmentFilter object containing the filter parameters to apply
     * @return the total count of appointments that match the specified filter
     * @throws DaoException if an error occurs while accessing the data source
     */
    int getCountOfAppointments(AppointmentFilter filter) throws DaoException;
}
