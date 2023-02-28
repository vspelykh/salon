package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.Connection;
import java.util.List;

public interface Dao<T> {

    T findById(int id) throws DaoException;

    List<T> findAll() throws DaoException;

    int create(T entity) throws DaoException;

    void update(T entity) throws DaoException;

    void removeById(int id) throws DaoException;

    void setConnection(Connection connection);
}
