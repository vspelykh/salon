package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface MarkDao extends Dao<Mark>{

    List<Mark> getMarksByMasterId(Integer masterId) throws DaoException;
}