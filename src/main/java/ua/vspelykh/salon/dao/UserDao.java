package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.MasterSort;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface UserDao extends Dao<User> {

    User findByEmailAndPassword(String email, String password) throws DaoException;

    User findByNumber(String number) throws DaoException;

    User findByEmail(String email) throws DaoException;

    List<User> findClients() throws DaoException;

    List<User> findMasters() throws DaoException;

    List<User> findMastersByLevelsAndServices(List<MastersLevel> levels, List<Integer> serviceIds,
                                              String search, int page, int size, MasterSort sort) throws DaoException;

    List<User> findAdministrators() throws DaoException;

    int getCountOfMasters(List<MastersLevel> levels, List<Integer> serviceIds, String search) throws DaoException;

    List<User> findBySearch(String search) throws DaoException;

    void updateRole(int userId, String action, Role role) throws DaoException;
}
