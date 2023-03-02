package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.entity.WorkingDay;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface WorkingDayService {

    List<WorkingDay> findDaysByUserId(Integer userId) throws ServiceException;

    void save(WorkingDay workingDay) throws ServiceException;

    void save(int userId, String[] datesArray, Time timeStart, Time timeEnd) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    WorkingDay getByUserIdAndDate(Integer userId, LocalDate date) throws ServiceException;

    void deleteWorkingDaysByUserIdAndDatesArray(int userId, String[] datesArray)throws ServiceException;

}
