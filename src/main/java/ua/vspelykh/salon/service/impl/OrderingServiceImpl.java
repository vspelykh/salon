package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dao.OrderingDao;
import ua.vspelykh.salon.model.Ordering;
import ua.vspelykh.salon.service.OrderingService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public class OrderingServiceImpl implements OrderingService {

    private static final Logger LOG = LogManager.getLogger(OrderingServiceImpl.class);

    private final OrderingDao  orderingDao;

    public OrderingServiceImpl() {
        orderingDao = DaoFactory.getOrderingDao();
    }

    @Override
    public void save(Ordering ordering) throws ServiceException {
        try {
            orderingDao.create(ordering);
        } catch (DaoException e) {
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
}
