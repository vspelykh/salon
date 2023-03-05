package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.entity.AppointmentItem;
import ua.vspelykh.salon.service.AppointmentItemService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

/**
 * Implementation of the AppointmentItemService interface.
 * This class provides methods to manipulate AppointmentItem objects in the system.
 *
 * @version 1.0
 */
public class AppointmentItemServiceImpl implements AppointmentItemService {

    private static final Logger LOG = LogManager.getLogger(AppointmentItemServiceImpl.class);

    private AppointmentItemDao appointmentItemDao;
    private Transaction transaction;

    /**
     * Saves a new AppointmentItem in the system.
     *
     * @param appointmentItem the to be saved
     * @throws ServiceException if there is an error while accessing the database or performing the transaction
     */
    @Override
    public void save(AppointmentItem appointmentItem) throws ServiceException {
        try {
            transaction.start();
            appointmentItemDao.create(appointmentItem);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save appointment item");
            throw new ServiceException(e);
        }
    }

    /**
     * Retrieves a list of AppointmentItem objects by the given appointment id.
     *
     * @param appointmentId the appointment id to search for
     * @return a list of AppointmentItem objects associated with the given appointment id
     * @throws ServiceException if there is an error while accessing the database
     */
    @Override
    public List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws ServiceException {
        try {
            return appointmentItemDao.getByAppointmentId(appointmentId);
        } catch (DaoException e) {
            LOG.error("Error to find appointment items by appointment id");
            throw new ServiceException(e);
        }
    }

    /**
     * Sets the AppointmentItemDao to be used by this service.
     *
     * @param appointmentItemDao the AppointmentItemDao to be set
     */
    public void setAppointmentItemDao(AppointmentItemDao appointmentItemDao) {
        this.appointmentItemDao = appointmentItemDao;
    }

    /**
     * Sets the Transaction to be used by this service,
     *
     * @param transaction the Transaction to be set
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}