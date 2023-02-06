package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.OrderingDao;
import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.service.OrderingService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.List;

public class OrderingServiceImpl implements OrderingService {

    private static final Logger LOG = LogManager.getLogger(OrderingServiceImpl.class);

    private OrderingDao orderingDao;
    private Transaction transaction;

    @Override
    public void save(Ordering ordering) throws ServiceException {
        try {
            transaction.start();
            orderingDao.create(ordering);
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
    public List<Ordering> getByAppointmentId(Integer appointmentId) throws ServiceException {
        try {
            return orderingDao.getByAppointmentId(appointmentId);
        } catch (DaoException e) {
            LOG.error("Error to find orderings by appointment id");
            throw new ServiceException(e);
        }
    }

    public void setOrderingDao(OrderingDao orderingDao) {
        this.orderingDao = orderingDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
