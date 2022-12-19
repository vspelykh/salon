package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.dao.impl.*;

public class DaoFactory {

    private DaoFactory() {
    }

    private static final UserDao userDao = new UserDaoImpl();
    private static final UserLevelDao levelDao = new UserLevelDaoImpl();
    private static final BaseServiceDao bsDao = new BaseServiceDaoImpl();
    private static final MasterServiceDao msDao = new MasterServiceDaoImpl();
    private static final AppointmentDao appointmentDao = new AppointmentDaoImpl();
    private static final OrderingDao orderingDao = new OrderingDaoImpl();
    private static final MarkDao markDao = new MarkDaoImpl();

    public static UserDao getUserDao() {
        return userDao;
    }

    public static UserLevelDao getUserLevelDao() {
        return levelDao;
    }

    public static BaseServiceDao getBaseServiceDao() {
        return bsDao;
    }

    public static MasterServiceDao getMasterServiceDao() {
        return msDao;
    }

    public static AppointmentDao getAppointmentDao() {
        return appointmentDao;
    }

    public static OrderingDao getOrderingDao() {
        return orderingDao;
    }

    public static MarkDao getMarkDao() {
        return markDao;
    }
}
