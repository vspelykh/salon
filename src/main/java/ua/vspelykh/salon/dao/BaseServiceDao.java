package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface BaseServiceDao extends Dao<BaseService> {

    List<BaseService> findByFilter(String name, Integer priceFrom, Integer priceTo) throws DaoException;

}