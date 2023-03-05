package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

/**
 * The WorkingDayService interface provides methods for managing working days for a master user.
 *
 * @version 1.0
 */
public interface WorkingDayService {

    /**
     * Finds all working days for the specified user by user ID.
     *
     * @param userId the ID of the user whose working days to retrieve
     * @return a list of all the user's working days
     * @throws ServiceException if an error occurs while retrieving the working days
     */
    List<WorkingDay> findByUserId(Integer userId) throws ServiceException;

    /**
     * Saves a working day for a user.
     *
     * @param workingDay the working day to save
     * @throws ServiceException if an error occurs while saving the working day
     */
    void save(WorkingDay workingDay) throws ServiceException;

    /**
     * Saves multiple working days for a user.
     *
     * @param userId     the ID of the user whose working days to save
     * @param datesArray an array of dates for the working days
     * @param timeStart  the start time for the working days
     * @param timeEnd    the end time for the working days
     * @throws ServiceException if an error occurs while saving the working days
     */
    void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws ServiceException;

    /**
     * Deletes a working day by ID.
     *
     * @param id the ID of the working day to delete
     * @throws ServiceException if an error occurs while deleting the working day
     */
    void delete(Integer id) throws ServiceException;

    /**
     * Retrieves a working day for a user by user ID and date.
     *
     * @param userId the ID of the user whose working day to retrieve
     * @param date   the date of the working day to retrieve
     * @return the working day for the specified user and date
     * @throws ServiceException if an error occurs while retrieving the working day
     */
    WorkingDay getByUserIdAndDate(Integer userId, LocalDate date) throws ServiceException;

    /**
     * Deletes multiple working days for a user by user ID and an array of dates.
     *
     * @param userId     the ID of the user whose working days to delete
     * @param datesArray an array of dates for the working days to delete
     * @throws ServiceException if an error occurs while deleting the working days
     */
    void deleteByUserIdAndDatesArray(int userId, String[] datesArray) throws ServiceException;
}
