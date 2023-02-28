package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface WorkingDayDao extends Dao<WorkingDay>{

    void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws DaoException;

    List<WorkingDay> getWorkingDaysByUserId(Integer userId) throws DaoException;

    WorkingDay getDayByUserIdAndDate(Integer userId, LocalDate date) throws DaoException;

    void deleteWorkingDaysByUserIdAndDatesArray(int userId, String[] datesArray) throws DaoException;

}
