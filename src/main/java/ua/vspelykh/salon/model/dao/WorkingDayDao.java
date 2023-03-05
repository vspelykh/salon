package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

/**
 * This interface represents a data access object for working day entities.
 *
 * @version 1.0
 */
public interface WorkingDayDao extends Dao<WorkingDay> {

    /**
     * Saves working days for a user with the given user ID, start time, end time, and array of dates in string format.
     *
     * @param userId     the ID of the user to save the working days for
     * @param datesArray an array of dates in string format
     * @param timeStart  the start time for the working days
     * @param timeEnd    the end time for the working days
     * @throws DaoException if there is an error accessing the data store
     */
    void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws DaoException;

    /**
     * Gets all working days for the user with the given user ID.
     *
     * @param userId the ID of the user to get working days for
     * @return a list of all working days for the user
     * @throws DaoException if there is an error accessing the data store
     */
    List<WorkingDay> getByUserId(Integer userId) throws DaoException;

    /**
     * Gets the working day for the user with the given user ID and date.
     *
     * @param userId the ID of the user to get the working day for
     * @param date   the date of the working day
     * @return the working day for the user with the given ID and date
     * @throws DaoException if there is an error accessing the data store
     */
    WorkingDay getByUserIdAndDate(Integer userId, LocalDate date) throws DaoException;

    /**
     * Deletes working days for a user with the given user ID and array of dates in string format.
     *
     * @param userId     the ID of the user to delete working days for
     * @param datesArray an array of dates in string format
     * @throws DaoException if there is an error accessing the data store
     */
    void deleteByUserIdAndDatesArray(int userId, String[] datesArray) throws DaoException;

}
