package ua.vspelykh.salon.service;

import ua.vspelykh.salon.dto.MasterServiceDto;
import ua.vspelykh.salon.model.Service;
import ua.vspelykh.salon.util.exception.ServiceException;

import java.util.List;

public interface ServiceService {

    Service findById(Integer id) throws ServiceException;

    List<Service> getAllByMasterId(Integer masterId) throws ServiceException;

    void save(Service service) throws ServiceException;

    List<Service> getAllByBaseServiceId(Integer baseServiceId) throws ServiceException;

    List<Service> findByFilter(List<Integer> userIds, List<Integer> serviceIds,
                               Integer continuanceFrom, Integer continuanceTo) throws ServiceException;

    void delete(Integer id) throws ServiceException;

    List<MasterServiceDto> getDTOsByMasterId(int masterId) throws ServiceException;
}
