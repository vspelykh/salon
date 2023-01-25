package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.BaseService;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class BaseServiceRowMapper implements RowMapper<BaseService> {

    @Override
    public BaseService map(ResultSet rs) throws SQLException {
        BaseService baseService = new BaseService();
        baseService.setId(rs.getInt(ID));
        baseService.setCategoryId(rs.getInt(CATEGORY_ID));
        baseService.setService(rs.getString(SERVICE));
        baseService.setServiceUa(rs.getString(SERVICE + UA));
        baseService.setPrice(rs.getInt(PRICE));
        return baseService;
    }
}
