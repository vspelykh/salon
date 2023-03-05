package ua.vspelykh.salon.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.BaseServiceDao;
import ua.vspelykh.salon.model.dao.MasterServiceDao;
import ua.vspelykh.salon.model.dao.ServiceCategoryDao;
import ua.vspelykh.salon.model.dto.BaseServiceDto;
import ua.vspelykh.salon.model.dto.MasterServiceDto;
import ua.vspelykh.salon.model.entity.BaseService;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.model.entity.ServiceCategory;
import ua.vspelykh.salon.service.MasterServiceService;
import ua.vspelykh.salon.service.Transaction;
import ua.vspelykh.salon.util.exception.DaoException;
import ua.vspelykh.salon.util.exception.ServiceException;
import ua.vspelykh.salon.util.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;

/**
 * The MasterServiceServiceImpl class implements the MasterServiceService interface.
 * It provides methods to interact with the MasterService entity and its related entities.
 *
 * @version 1.0
 */
public class MasterServiceServiceImpl implements MasterServiceService {

    private static final Logger LOG = LogManager.getLogger(MasterServiceServiceImpl.class);

    private ServiceCategoryDao serviceCategoryDao;
    private MasterServiceDao masterServiceDao;
    private BaseServiceDao baseServiceDao;
    private Transaction transaction;

    /**
     * Returns the MasterService entity with the specified ID.
     *
     * @param id the ID of the MasterService entity
     * @return the MasterService entity with the specified ID
     * @throws ServiceException if there was an error while retrieving the MasterService entity from the database
     */
    @Override
    public MasterService findById(Integer id) throws ServiceException {
        try {
            return masterServiceDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Saves the specified MasterService entity to the database.
     *
     * @param masterService the MasterService entity to be saved
     * @throws ServiceException if there was an error while saving the MasterService entity to the database
     */
    @Override
    public void save(MasterService masterService) throws ServiceException {
        try {
            transaction.start();
            if (masterService.isNew()) {
                masterServiceDao.create(masterService);
            } else {
                masterServiceDao.update(masterService);
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

    /**
     * Deletes the MasterService entity with the specified ID from the database.
     *
     * @param id the ID of the MasterService entity to be deleted
     * @throws ServiceException if there was an error while deleting the MasterService entity from the database
     */
    @Override
    public void delete(Integer id) throws ServiceException {
        try {
            transaction.start();
            masterServiceDao.removeById(id);
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

    /**
     * Returns a list of MasterServiceDto objects associated with the specified master ID and locale.
     *
     * @param masterId the ID of the master
     * @param locale   the locale in which to retrieve the MasterServiceDto objects
     * @return a list of MasterServiceDto objects associated with the specified master ID and locale
     * @throws ServiceException if there was an error while retrieving the MasterServiceDto objects from the database
     */
    @Override
    public List<MasterServiceDto> getDTOsByMasterId(int masterId, String locale) throws ServiceException {
        try {
            transaction.start();
            List<MasterService> masterServices = masterServiceDao.getAllByUserId(masterId);
            List<MasterServiceDto> dtos = new ArrayList<>();
            for (MasterService masterService : masterServices) {
                BaseService baseService = baseServiceDao.findById(masterService.getBaseServiceId());
                ServiceCategory category = serviceCategoryDao.findById(baseService.getCategoryId());
                BaseServiceDto baseServiceDto = new BaseServiceDto.BaseServiceDtoBuilder(baseService, category, locale).build();
                MasterServiceDto dto = toDTO(masterService, baseServiceDto);
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
            throw new ServiceException(e);
        }
    }

    /**
     * Converts a MasterService object to a MasterServiceDto object.
     *
     * @param masterService  the MasterService object to convert
     * @param baseServiceDto information about service
     * @return a MasterServiceDto object
     */
    private MasterServiceDto toDTO(MasterService masterService, BaseServiceDto baseServiceDto) {
        return MasterServiceDto.builder()
                .id(masterService.getId())
                .masterId(masterService.getMasterId())
                .service(baseServiceDto)
                .continuance(masterService.getContinuance())
                .build();
    }

    public void setMasterServiceDao(MasterServiceDao masterServiceDao) {
        this.masterServiceDao = masterServiceDao;
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
