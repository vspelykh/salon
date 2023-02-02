package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.WorkingDayDao;
import ua.vspelykh.salon.model.WorkingDay;
import ua.vspelykh.salon.service.WorkingDayService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class WorkingDayServiceImpl implements WorkingDayService {

    private static final Logger LOG = LogManager.getLogger(WorkingDayServiceImpl.class);

    private WorkingDayDao workingDayDao;

    @Override
    public List<WorkingDay> findDaysByUserId(Integer userId) throws ServiceException {
        try {
            return workingDayDao.getWorkingDaysByUserId(userId);
        } catch (DaoException e) {
            //TODO
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(WorkingDay workingDay) throws ServiceException {
        try {
            workingDayDao.create(workingDay);
        } catch (DaoException e) {
            LOG.error("Error to save");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws ServiceException {
        try {
            workingDayDao.save(userId, datesArray, timeStart, timeEnd);
        } catch (DaoException e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            workingDayDao.removeById(id);
        } catch (DaoException e) {
            LOG.error("Error to delete mark");
            throw new ServiceException(e);
        }
    }

    @Override
    public WorkingDay getDayByUserIdAndDate(Integer userId, LocalDate date) throws ServiceException {
        try {
            return workingDayDao.getDayByUserIdAndDate(userId, date);
        } catch (DaoException e) {
            return null;
        }
    }

    @Override
    public void deleteWorkingDaysByUserIdAndDatesArray(int userId, String[] datesArray) {
        try {
            workingDayDao.deleteWorkingDaysByUserIdAndDatesArray(userId, datesArray);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public void setWorkingDayDao(WorkingDayDao workingDayDao) {
        this.workingDayDao = workingDayDao;
    }
}
