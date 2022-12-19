package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface MarkService {

    void save(Mark mark) throws ServiceException;

    List<Mark> getMarksByMasterId(Integer masterId) throws ServiceException;

    void delete(Integer id) throws ServiceException;
}
