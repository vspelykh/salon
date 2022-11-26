package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.util.exception.DaoException;

public interface Dao<T> {

    T findById(int id) throws DaoException;

    T findAll() throws DaoException;

    long save(T item) throws DaoException;

    void removeById(int id) throws DaoException;
}
