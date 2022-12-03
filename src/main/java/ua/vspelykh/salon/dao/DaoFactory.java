package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.dao.impl.UserDaoImpl;
import ua.vspelykh.salon.dao.impl.UserLevelDaoImpl;

public class DaoFactory {

    private static final UserDao userDao = new UserDaoImpl();
    private static final UserLevelDao levelDao = new UserLevelDaoImpl();

    public static UserDao getUserDao() {
        return userDao;
    }

    public static UserLevelDao getUserLevelDao() {
        return levelDao;
    }
}
