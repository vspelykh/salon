package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface BaseServiceDao extends Dao<BaseService> {

    List<BaseService> findByFilter(List<Integer> categoriesIds, int page, int size) throws DaoException;

    int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws DaoException;
}
