package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface UserDao extends Dao<User> {

    User findByEmailAndPassword(String email, String password) throws DaoException;

    User findByNumber(String number) throws DaoException;

    User findByEmail(String email) throws DaoException;

    List<User> findClients() throws DaoException;

    List<User> findMasters() throws DaoException;

    List<User> findAdministrators() throws DaoException;

}
