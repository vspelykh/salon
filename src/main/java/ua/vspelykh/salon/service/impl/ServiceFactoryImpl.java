package ua.vspelykh.salon.service.impl;

import ua.vspelykh.salon.model.dao.*;
import ua.vspelykh.salon.model.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.model.dao.impl.*;
import ua.vspelykh.salon.service.*;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Connection;
import java.sql.SQLException;

public class ServiceFactoryImpl implements ServiceFactory {

    private Connection connection;

    @Override
    public UserService getUserService() throws ServiceException {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(getUserDao());
        userService.setMarkDao(getMarkDao());
        userService.setUserLevelDao(getUserLevelDao());
        userService.setInvitationDao(getInvitationDao());
        userService.setTransaction(getTransaction());
        return userService;
    }

    @Override
    public AppointmentService getAppointmentService() throws ServiceException {
        AppointmentServiceImpl appointmentService = new AppointmentServiceImpl();
        appointmentService.setAppointmentDao(getAppointmentDao());
        appointmentService.setOrderingDao(getOrderingDao());
        appointmentService.setUserDao(getUserDao());
        appointmentService.setTransaction(getTransaction());
        return appointmentService;
    }

    @Override
    public BaseServiceService getBaseServiceService() throws ServiceException {
        BaseServiceServiceImpl baseServiceService = new BaseServiceServiceImpl();
        baseServiceService.setBaseServiceDao(getBaseServiceDao());
        baseServiceService.setServiceCategoryDao(getServiceCategoryDao());
        baseServiceService.setTransaction(getTransaction());
        return baseServiceService;
    }

    @Override
    public ConsultationService getConsultationService() throws ServiceException {
        ConsultationServiceImpl consultationService = new ConsultationServiceImpl();
        consultationService.setDao(getConsultationDao());
        consultationService.setTransaction(getTransaction());
        return consultationService;
    }

    @Override
    public InvitationService getInvitationService() throws ServiceException {
        InvitationServiceImpl invitationService = new InvitationServiceImpl();
        invitationService.setInvitationDao(getInvitationDao());
        invitationService.setTransaction(getTransaction());
        return invitationService;
    }

    @Override
    public FeedbackService getFeedbackService() throws ServiceException {
        FeedbackServiceImpl markService = new FeedbackServiceImpl();
        markService.setFeedbackDao(getMarkDao());
        markService.setAppointmentDao(getAppointmentDao());
        markService.setUserDao(getUserDao());
        markService.setTransaction(getTransaction());
        return markService;
    }

    @Override
    public AppointmentItemService getOrderingService() throws ServiceException {
        AppointmentItemServiceImpl orderingService = new AppointmentItemServiceImpl();
        orderingService.setAppointmentItemDao(getOrderingDao());
        orderingService.setTransaction(getTransaction());
        return orderingService;
    }

    @Override
    public ServiceCategoryService getServiceCategoryService() throws ServiceException {
        ServiceCategoryServiceImpl serviceCategoryService = new ServiceCategoryServiceImpl();
        serviceCategoryService.setServiceCategoryDao(getServiceCategoryDao());
        serviceCategoryService.setTransaction(getTransaction());
        return serviceCategoryService;
    }

    @Override
    public MasterServiceService getServiceService() throws ServiceException {
        MasterServiceServiceImpl serviceService = new MasterServiceServiceImpl();
        serviceService.setMsDao(getMasterServiceDao());
        serviceService.setBaseServiceDao(getBaseServiceDao());
        serviceService.setServiceCategoryDao(getServiceCategoryDao());
        serviceService.setTransaction(getTransaction());
        return serviceService;
    }

    @Override
    public WorkingDayService getWorkingDayService() throws ServiceException {
        WorkingDayServiceImpl workingDayService = new WorkingDayServiceImpl();
        workingDayService.setWorkingDayDao(getWorkingDayDao());
        workingDayService.setTransaction(getTransaction());
        return workingDayService;
    }

    @Override
    public Transaction getTransaction() throws ServiceException {
        TransactionImpl transaction = new TransactionImpl();
        transaction.setConnection(getConnection());
        return transaction;
    }

    @Override
    public UserDao getUserDao() throws ServiceException {
        UserDaoImpl userDao = new UserDaoImpl();
        userDao.setConnection(getConnection());
        return userDao;
    }

    @Override
    public UserLevelDao getUserLevelDao() throws ServiceException {
        UserLevelDaoImpl userLevelDao = new UserLevelDaoImpl();
        userLevelDao.setConnection(getConnection());
        return userLevelDao;
    }

    @Override
    public BaseServiceDao getBaseServiceDao() throws ServiceException {
        BaseServiceDaoImpl bsDao = new BaseServiceDaoImpl();
        bsDao.setConnection(getConnection());
        return bsDao;
    }

    @Override
    public MasterServiceDao getMasterServiceDao() throws ServiceException {
        MasterServiceDaoImpl msDao = new MasterServiceDaoImpl();
        msDao.setConnection(getConnection());
        return msDao;
    }

    @Override
    public AppointmentDao getAppointmentDao() throws ServiceException {
        AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();
        appointmentDao.setConnection(getConnection());
        return appointmentDao;
    }

    @Override
    public AppointmentItemDao getOrderingDao() throws ServiceException {
        AppointmentItemDaoImpl orderingDao = new AppointmentItemDaoImpl();
        orderingDao.setConnection(getConnection());
        return orderingDao;
    }

    @Override
    public FeedbackDao getMarkDao() throws ServiceException {
        FeedbackDaoImpl markDao = new FeedbackDaoImpl();
        markDao.setConnection(getConnection());
        return markDao;
    }

    @Override
    public ConsultationDao getConsultationDao() throws ServiceException {
        ConsultationDaoImpl consultationDao = new ConsultationDaoImpl();
        consultationDao.setConnection(getConnection());
        return consultationDao;
    }

    @Override
    public WorkingDayDao getWorkingDayDao() throws ServiceException {
        WorkingDayDaoImpl wdd = new WorkingDayDaoImpl();
        wdd.setConnection(getConnection());
        return wdd;
    }

    @Override
    public ServiceCategoryDao getServiceCategoryDao() throws ServiceException {
        ServiceCategoryDaoImpl serviceCategoryDao = new ServiceCategoryDaoImpl();
        serviceCategoryDao.setConnection(getConnection());
        return serviceCategoryDao;
    }

    @Override
    public InvitationDao getInvitationDao() throws ServiceException {
        InvitationDaoImpl invitationDao = new InvitationDaoImpl();
        invitationDao.setConnection(getConnection());
        return invitationDao;
    }

    @Override
    public Connection getConnection() throws ServiceException {
        if (connection == null) {
            try {
                connection = DBCPDataSource.getConnection();
            } catch (SQLException e) {
                throw new ServiceException(e);
            }
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
            connection = null;
        } catch (Exception e) {
            /*ignore*/
        }
    }

    public static ServiceFactory getServiceFactory() {
        return new ServiceFactoryImpl();
    }
}
