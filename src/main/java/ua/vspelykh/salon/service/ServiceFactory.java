package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dao.*;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.sql.Connection;

/**
 * The ServiceFactory interface provides methods for creating instances of service and data access objects, as well as
 * managing transactions and database connections.
 *
 * @version 1.0
 */
public interface ServiceFactory extends AutoCloseable {

    UserService getUserService() throws ServiceException;

    AppointmentService getAppointmentService() throws ServiceException;

    BaseServiceService getBaseServiceService() throws ServiceException;

    ConsultationService getConsultationService() throws ServiceException;

    InvitationService getInvitationService() throws ServiceException;

    FeedbackService getFeedbackService() throws ServiceException;

    ServiceCategoryService getServiceCategoryService() throws ServiceException;

    MasterServiceService getServiceService() throws ServiceException;

    WorkingDayService getWorkingDayService() throws ServiceException;

    Transaction getTransaction() throws ServiceException;

    UserDao getUserDao() throws ServiceException;

    UserLevelDao getUserLevelDao() throws ServiceException;

    BaseServiceDao getBaseServiceDao() throws ServiceException;

    MasterServiceDao getMasterServiceDao() throws ServiceException;

    AppointmentDao getAppointmentDao() throws ServiceException;

    AppointmentItemDao getOrderingDao() throws ServiceException;

    FeedbackDao getFeedbackDao() throws ServiceException;

    ConsultationDao getConsultationDao() throws ServiceException;

    WorkingDayDao getWorkingDayDao() throws ServiceException;

    ServiceCategoryDao getServiceCategoryDao() throws ServiceException;

    InvitationDao getInvitationDao() throws ServiceException;

    Connection getConnection() throws ServiceException;
}
