package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface Dao<T> {

    T findById(int id) throws DaoException;

    List<T> findAll() throws DaoException;

    int create(T entity) throws DaoException;

    void update(T entity) throws DaoException;

    void removeById(int id) throws DaoException;
}
