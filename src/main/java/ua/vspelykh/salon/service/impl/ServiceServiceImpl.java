package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.BaseServiceDao;
import ua.vspelykh.salon.dao.MasterServiceDao;
import ua.vspelykh.salon.dao.ServiceCategoryDao;
import ua.vspelykh.salon.dto.BaseServiceDto;
import ua.vspelykh.salon.dto.MasterServiceDto;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.model.MasterService;
import ua.vspelykh.salon.model.ServiceCategory;
import ua.vspelykh.salon.service.ServiceService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

public class ServiceServiceImpl implements ServiceService {

    private static final Logger LOG = LogManager.getLogger(ServiceServiceImpl.class);

    private ServiceCategoryDao serviceCategoryDao;
    private MasterServiceDao msDao;
    private BaseServiceDao baseServiceDao;
    private Transaction transaction;

    @Override
    public MasterService findById(Integer id) throws ServiceException {
        try {
            return msDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MasterService> getAllByMasterId(Integer masterId) throws ServiceException {
        try {
            return msDao.getAllByUserId(masterId);
        } catch (DaoException e) {
            LOG.error("Error to get services by master_id");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(MasterService masterService) throws ServiceException {
        try {
            transaction.start();
            if (masterService.isNew()) {
                msDao.create(masterService);
            } else {
                msDao.update(masterService);
            }
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to save service");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MasterService> getAllByBaseServiceId(Integer baseServiceId) throws ServiceException {
        try {
            return msDao.getAllByBaseServiceId(baseServiceId);
        } catch (DaoException e) {
            LOG.error("Error to get service by base service");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MasterService> findByFilter(List<Integer> userIds, List<Integer> serviceIds, Integer continuanceFrom,
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
            transaction.start();
            msDao.removeById(id);
            transaction.commit();
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            LOG.error("Error to delete service of master");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<MasterServiceDto> getDTOsByMasterId(int masterId, String locale) throws ServiceException {
        try {
            transaction.start();
            List<MasterService> masterServices = msDao.getAllByUserId(masterId);
            List<MasterServiceDto> dtos = new ArrayList<>();
            for (MasterService masterService : masterServices) {
                BaseService baseService = baseServiceDao.findById(masterService.getBaseServiceId());
                ServiceCategory category = serviceCategoryDao.findById(baseService.getCategoryId());
                BaseServiceDto baseServiceDto = new BaseServiceDto.BaseServiceDtoBuilder(baseService, category, locale).build();
                MasterServiceDto dto = new MasterServiceDto(masterService.getId(), masterService.getMasterId(), baseServiceDto, masterService.getContinuance());
                dtos.add(dto);
            }
            transaction.commit();
            return dtos;
        } catch (DaoException | TransactionException e) {
            try {
                transaction.rollback();
            } catch (TransactionException ex) {
                /*ignore*/
            }
            //TODO
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public void setMsDao(MasterServiceDao msDao) {
        this.msDao = msDao;
    }

    public void setBaseServiceDao(BaseServiceDao baseServiceDao) {
        this.baseServiceDao = baseServiceDao;
    }

    public void setServiceCategoryDao(ServiceCategoryDao serviceCategoryDao) {
        this.serviceCategoryDao = serviceCategoryDao;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
