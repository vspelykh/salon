package ua.vspelykh.salon.service;

import ua.vspelykh.salon.dao.*;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Connection;

public interface ServiceFactory extends AutoCloseable {

    UserService getUserService() throws ServiceException;

    AppointmentService getAppointmentService() throws ServiceException;

    BaseServiceService getBaseServiceService() throws ServiceException;
    ConsultationService getConsultationService() throws ServiceException;
    InvitationService getInvitationService() throws ServiceException;
    MarkService getMarkService() throws ServiceException;
    OrderingService getOrderingService() throws ServiceException;
    ServiceCategoryService getServiceCategoryService() throws ServiceException;
    ServiceService getServiceService() throws ServiceException;
    WorkingDayService getWorkingDayService() throws ServiceException;

    Transaction getTransaction() throws ServiceException;

    UserDao getUserDao() throws ServiceException;
    UserLevelDao getUserLevelDao() throws ServiceException;
    BaseServiceDao getBaseServiceDao() throws ServiceException;
    MasterServiceDao getMasterServiceDao() throws ServiceException;
    AppointmentDao getAppointmentDao() throws ServiceException;
    OrderingDao getOrderingDao() throws ServiceException;
    MarkDao getMarkDao() throws ServiceException;
    ConsultationDao getConsultationDao() throws ServiceException;
    WorkingDayDao getWorkingDayDao() throws ServiceException;

    ServiceCategoryDao getServiceCategoryDao() throws ServiceException;

    InvitationDao getInvitationDao() throws ServiceException;
    Connection getConnection() throws ServiceException;
}