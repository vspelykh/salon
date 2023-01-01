package ua.vspelykh.salon;

import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.impl.UserDaoImpl;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        for (int i = 1; i <= 11111; i++) {
            try {
                System.out.println(i + " " + new UserDaoImpl().findAll());
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }
}
