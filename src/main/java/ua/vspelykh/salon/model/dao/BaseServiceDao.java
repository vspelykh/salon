package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface BaseServiceDao extends Dao<BaseService> {

    List<BaseService> findByFilter(List<Integer> categoriesIds, int page, int size) throws DaoException;

    int getCountOfCategories(List<Integer> categoriesIds, int page, int size) throws DaoException;
}
