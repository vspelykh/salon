package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.model.MasterService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface MasterServiceDao extends Dao<MasterService> {

    List<MasterService> getAllByUserId(Integer userId) throws DaoException;

    List<MasterService> getAllByBaseServiceId(Integer baseServiceId) throws DaoException;

    List<MasterService> findByFilter(List<Integer> userIds, List<Integer> serviceIds,
                                     Integer continuanceFrom, Integer continuanceTo) throws DaoException;
}
