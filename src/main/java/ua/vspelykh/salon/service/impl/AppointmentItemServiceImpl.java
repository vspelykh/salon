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

public class AppointmentItemServiceImpl implements AppointmentItemService {

    private static final Logger LOG = LogManager.getLogger(AppointmentItemServiceImpl.class);

    private AppointmentItemDao appointmentItemDao;
    private Transaction transaction;

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
            LOG.error("Error to save ordering");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<AppointmentItem> getByAppointmentId(Integer appointmentId) throws ServiceException {
        try {
            return appointmentItemDao.getByAppointmentId(appointmentId);
        } catch (DaoException e) {
            LOG.error("Error to find appointment items by appointment id");
            throw new ServiceException(e);
        }
    }



    public void setAppointmentItemDao(AppointmentItemDao appointmentItemDao) {
        this.appointmentItemDao = appointmentItemDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
