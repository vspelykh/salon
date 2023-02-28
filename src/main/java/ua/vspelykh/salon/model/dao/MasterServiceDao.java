package ua.vspelykh.salon.model.dao;

import ua.vspelykh.salon.model.entity.MasterService;
import ua.vspelykh.salon.util.exception.DaoException;

import java.util.List;

public interface MasterServiceDao extends Dao<MasterService> {

    List<MasterService> getAllByUserId(Integer userId) throws DaoException;

    List<MasterService> getAllByBaseServiceId(Integer baseServiceId) throws DaoException;
}
