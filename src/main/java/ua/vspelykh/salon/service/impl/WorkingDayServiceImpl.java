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

public class WorkingDayServiceImpl implements WorkingDayService {

    private static final Logger LOG = LogManager.getLogger(WorkingDayServiceImpl.class);

    private WorkingDayDao workingDayDao;
    private Transaction transaction;

    @Override
    public List<WorkingDay> findDaysByUserId(Integer userId) throws ServiceException {
        try {
            return workingDayDao.getWorkingDaysByUserId(userId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

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

    @Override
    public WorkingDay getDayByUserIdAndDate(Integer userId, LocalDate date) {
        try {
            return workingDayDao.getDayByUserIdAndDate(userId, date);
        } catch (DaoException e) {
            return null;
        }
    }

    @Override
    public void deleteWorkingDaysByUserIdAndDatesArray(int userId, String[] datesArray) throws ServiceException {
        try {
            transaction.start();
            workingDayDao.deleteWorkingDaysByUserIdAndDatesArray(userId, datesArray);
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
