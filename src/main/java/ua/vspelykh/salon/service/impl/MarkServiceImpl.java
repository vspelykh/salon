package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dao.MarkDao;
import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.service.MarkService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public class MarkServiceImpl implements MarkService {

    private static final Logger LOG = LogManager.getLogger(MarkServiceImpl.class);

    private final MarkDao markDao;

    public MarkServiceImpl() {
        markDao = DaoFactory.getMarkDao();
    }

    @Override
    public void save(Mark mark) throws ServiceException {
        try {
            markDao.create(mark);
        } catch (DaoException e) {
            LOG.error("Error to save mark");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Mark> getMarksByMasterId(Integer masterId) throws ServiceException {
        try {
            return markDao.getMarksByMasterId(masterId);
        } catch (DaoException e) {
            LOG.error("Error to get marks for master");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            markDao.removeById(id);
        } catch (DaoException e) {
            LOG.error("Error to delete mark");
            throw new ServiceException(e);
        }
    }
}
