package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.WorkingDayDao;
import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

/**
 * This class implements the WorkingDayService interface, providing methods for managing working days.
 * It relies on a WorkingDayDao object to interact with the data layer and a Transaction object to handle transactions.
 *
 * @version 1.0
 */
public class WorkingDayServiceImpl implements WorkingDayService {

    private static final Logger LOG = LogManager.getLogger(WorkingDayServiceImpl.class);

    private WorkingDayDao workingDayDao;
    private Transaction transaction;

    /**
     * Returns a list of WorkingDay objects associated with the given user id.
     *
     * @param userId The id of the user for which to retrieve working days.
     * @return A list of WorkingDay objects associated with the given user id.
     * @throws ServiceException if an error occurs while retrieving the working days.
     */
    @Override
    public List<WorkingDay> findByUserId(Integer userId) throws ServiceException {
        try {
            return workingDayDao.getByUserId(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Saves a new working day.
     *
     * @param workingDay The WorkingDay object to be saved.
     * @throws ServiceException if an error occurs while saving the working day.
     */
    @Override
    public void save(WorkingDay workingDay) throws ServiceException {
        try {
            transaction.start();
            workingDayDao.create(workingDay);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save");
            throw new ServiceException(e);
        }
    }

    /**
     * Saves multiple working days for a given user id.
     *
     * @param userId     The id of the user for which to save working days.
     * @param datesArray An array of strings representing the dates for which to save working days.
     * @param timeStart  The starting time for the working days.
     * @param timeEnd    The ending time for the working days.
     * @throws ServiceException if an error occurs while saving the working days.
     */
    @Override
    public void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws ServiceException {
        try {
            transaction.start();
            workingDayDao.save(userId, datesArray, timeStart, timeEnd);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
        }
    }

    /**
     * Deletes a working day with the given id.
     *
     * @param id The id of the working day to be deleted.
     * @throws ServiceException if an error occurs while deleting the working day.
     */
    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            transaction.start();
            workingDayDao.removeById(id);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to delete mark");
            throw new ServiceException(e);
        }
    }

    /**
     * Returns the WorkingDay object associated with the given user id and date.
     *
     * @param userId The id of the user for which to retrieve the working day.
     * @param date   The date of the working day to be retrieved.
     * @return The WorkingDay object associated with the given user id and date, or null if not found.
     */
    @Override
    public WorkingDay getByUserIdAndDate(Integer userId, LocalDate date) {
        try {
            return workingDayDao.getByUserIdAndDate(userId, date);
        } catch (DaoException e) {
            return null;
        }
    }

    /**
     * Deletes a list of working days for a given user ID and array of dates.
     *
     * @param userId     the ID of the user whose working days to delete
     * @param datesArray the array of dates for which to delete working days
     * @throws ServiceException if there was an error while accessing the database or performing a transaction
     */
    @Override
    public void deleteByUserIdAndDatesArray(int userId, String[] datesArray) throws ServiceException {
        try {
            transaction.start();
            workingDayDao.deleteByUserIdAndDatesArray(userId, datesArray);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            throw new ServiceException(e);
        }
    }

    public void setWorkingDayDao(WorkingDayDao workingDayDao) {
        this.workingDayDao = workingDayDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
