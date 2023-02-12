package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AppointmentItemDao;
import ua.vspelykh.salon.model.AppointmentItem;
import ua.vspelykh.salon.service.OrderingService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

public class OrderingServiceImpl implements OrderingService {

    private static final Logger LOG = LogManager.getLogger(OrderingServiceImpl.class);

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
            LOG.error("Error to find orderings by appointment id");
            throw new ServiceException(e);
        }
    }

    public void setOrderingDao(AppointmentItemDao appointmentItemDao) {
        this.appointmentItemDao = appointmentItemDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
