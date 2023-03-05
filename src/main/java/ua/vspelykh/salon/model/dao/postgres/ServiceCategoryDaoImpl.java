package ua.vspelykh.salon.model.dao.postgres;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.util.exception.DaoException;

/**
 * Implementation of ServiceCategoryDao interface. Provides CRUD operations with ServiceCategory
 * using DAO pattern. Extends  AbstractDao class.
 *
 * @version 1.0
 */
public class ServiceCategoryDaoImpl extends AbstractDao<ServiceCategory> implements ServiceCategoryDao {

    private static final Logger LOG = LogManager.getLogger(ServiceCategoryDaoImpl.class);

    /**
     * Constructor which creates an instance of ServiceCategoryDaoImpl class using RowMapperFactory
     * and Table constants.
     */
    public ServiceCategoryDaoImpl() {
        super(RowMapperFactory.getServiceCategoryRowMapper(), Table.SERVICE_CATEGORY);
    }


    @Override
    public int create(ServiceCategory entity) throws DaoException {
        LOG.error("Create service category is unsupported operation!");
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(ServiceCategory entity) throws DaoException {
        LOG.error("Update service category is unsupported operation!");
        throw new UnsupportedOperationException();
    }
}
