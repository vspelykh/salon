package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dao.MasterServiceDao;
import ua.vspelykh.salon.dto.MasterServiceDto;
import ua.vspelykh.salon.model.Service;
import ua.vspelykh.salon.service.ServiceService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public class ServiceServiceImpl implements ServiceService {

    private static final Logger LOG = LogManager.getLogger(ServiceServiceImpl.class);

    private final MasterServiceDao msDao;

    public ServiceServiceImpl() {
        this.msDao = DaoFactory.getMasterServiceDao();
    }

    @Override
    public Service findById(Integer id) throws ServiceException {
        try {
            return msDao.findById(id);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Service> getAllByMasterId(Integer masterId) throws ServiceException {
        try {
            return msDao.getAllByUserId(masterId);
        } catch (DaoException e) {
            LOG.error("Error to get services by master_id");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(Service service) throws ServiceException {
        try {
            if (service.isNew()) {
                msDao.create(service);
            } else msDao.update(service);
        } catch (DaoException e){
            LOG.error("Error to save service");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Service> getAllByBaseServiceId(Integer baseServiceId) throws ServiceException {
        try {
            return msDao.getAllByBaseServiceId(baseServiceId);
        } catch (DaoException e) {
            LOG.error("Error to get service by base service");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Service> findByFilter(List<Integer> userIds, List<Integer> serviceIds, Integer continuanceFrom,
                                      Integer continuanceTo) throws ServiceException {
        try {
            return msDao.findByFilter(userIds, serviceIds, continuanceFrom, continuanceTo);
        } catch (DaoException e) {
            LOG.error("Error to get services by filter");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            msDao.removeById(id);
        } catch (DaoException e) {
            LOG.error("Error to delete service of master");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MasterServiceDto> getDTOsByMasterId(int masterId) throws ServiceException {
        try {
            return msDao.getDTOsByMasterId(masterId);
        } catch (DaoException e) {
            //TODO
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }
}
