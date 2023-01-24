package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.BaseServiceDao;
import ua.vspelykh.salon.dao.DaoFactory;
import ua.vspelykh.salon.dto.BaseServiceDto;
import ua.vspelykh.salon.model.BaseService;
import ua.vspelykh.salon.service.BaseServiceService;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseServiceServiceImpl implements BaseServiceService {

    private static final Logger LOG = LogManager.getLogger(BaseServiceServiceImpl.class);

    private final BaseServiceDao bsDao;

    public BaseServiceServiceImpl() {
        this.bsDao = DaoFactory.getBaseServiceDao();
    }

    @Override
    public BaseService findById(Integer id) throws ServiceException {
        try {
            return bsDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseServiceDto> findAll(String locale) throws ServiceException {
        try {
            return toDtos(bsDao.findAll(), locale);
        } catch (DaoException e) {
            LOG.error("Error to get all base services");
            throw new ServiceException(e);
        }
    }

    @Override
    public void save(BaseService baseService) throws ServiceException {
        try {
            if (baseService.isNew()) {
                bsDao.create(baseService);
            } else bsDao.update(baseService);
        } catch (DaoException e) {
            LOG.error("Error to save base service");
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Integer baseServiceId) throws ServiceException {
        try {
            bsDao.removeById(baseServiceId);
        } catch (DaoException e) {
            LOG.error("Error to delete base service by id");
            throw new ServiceException(e);
        }
    }

    @Override
    public List<BaseService> findByFilter(String name, Integer priceFrom, Integer priceTo) throws ServiceException {
        try {
            return bsDao.findByFilter(name, priceFrom, priceTo);
        } catch (DaoException e) {
            LOG.error("Error to get base services by filter");
            throw new ServiceException(e);
        }
    }

    private List<BaseServiceDto> toDtos(List<BaseService> baseServices, String locale){
        List<BaseServiceDto> dtos = new ArrayList<>();
        for (BaseService baseService : baseServices){
            dtos.add(toDto(baseService, locale));
        }
        return dtos;
    }

    private BaseServiceDto toDto(BaseService baseService, String locale) {
        BaseServiceDto dto = new BaseServiceDto();
        dto.setId(baseService.getId());
        if (Objects.equals(locale, "ua")) {
            dto.setService(baseService.getServiceUa());
        } else {
            dto.setService(baseService.getService());
        }
        dto.setPrice(baseService.getPrice());
        return dto;
    }
}
