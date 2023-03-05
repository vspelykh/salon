package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

/**
 * This interface extends the Dao interface for the AppointmentItem entity type,adding a method to retrieve
 * a list of appointment items by appointment ID.
 *
 * @version 1.0
 */
public interface AppointmentItemDao extends Dao<AppointmentItem> {

    /**
     * Returns a list of appointment items for the specified appointment ID.
     *
     * @param appointmentId the ID of the appointment to retrieve items for
     * @return a list of appointment items for the specified appointment ID
     * @throws DaoException if an error occurs while accessing the data source
     */
    List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws DaoException;
}
