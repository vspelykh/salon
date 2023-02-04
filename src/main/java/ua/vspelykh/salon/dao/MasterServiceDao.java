package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.dto.MasterServiceDto;
import ua.vspelykh.salon.model.Service;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface MasterServiceDao extends Dao<Service> {

    List<Service> getAllByUserId(Integer userId) throws DaoException;

    List<Service> getAllByBaseServiceId(Integer baseServiceId) throws DaoException;

    List<Service> findByFilter(List<Integer> userIds, List<Integer> serviceIds,
                               Integer continuanceFrom, Integer continuanceTo) throws DaoException;
}
