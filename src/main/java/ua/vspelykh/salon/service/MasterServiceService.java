package ua.vspelykh.salon.service;

import ua.vspelykh.salon.model.dto.MasterServiceDto;
import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface MasterServiceService {

    MasterService findById(Integer id) throws ServiceException;

    List<MasterService> getAllByMasterId(Integer masterId) throws ServiceException;

    void save(MasterService masterService) throws ServiceException;

    List<MasterService> getAllByBaseServiceId(Integer baseServiceId) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    List<MasterServiceDto> getDTOsByMasterId(int masterId, String locale) throws ServiceException;
}
