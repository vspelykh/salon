package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

/**
 * This interface defines the methods to manage appointment items.
 *
 * @version 1.0
 */
public interface AppointmentItemService {

    /**
     * Saves the specified appointment item.
     *
     * @param appointmentItem the appointment item to be saved
     * @throws ServiceException if an error occurs while saving the appointment item
     */
    void save(AppointmentItem appointmentItem) throws ServiceException;

    /**
     * Retrieves a list of appointment items that belong to the specified appointment ID.
     *
     * @param appointmentId the ID of the appointment for which to retrieve the items
     * @return a list of appointment items that belong to the specified appointment ID
     * @throws ServiceException if an error occurs while retrieving the appointment items
     */
    List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws ServiceException;
}
