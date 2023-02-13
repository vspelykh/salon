package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.util.exception.DaoException;

public class ServiceCategoryDaoImpl extends AbstractDao<ServiceCategory> implements ServiceCategoryDao {

    private static final Logger LOG = LogManager.getLogger(ServiceCategoryDaoImpl.class);


    public ServiceCategoryDaoImpl() {
        super(RowMapperFactory.getServiceCategoryRowMapper(), Table.SERVICE_CATEGORY);;
    }


    @Override
    public int create(ServiceCategory entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(ServiceCategory entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

}
