package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.BaseService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseServiceRowMapper implements RowMapper<BaseService> {

    @Override
    public BaseService map(ResultSet rs) throws SQLException {
        BaseService baseService = new BaseService();
        baseService.setId(rs.getInt(Column.ID));
        baseService.setCategoryId(rs.getInt(Column.CATEGORY_ID));
        baseService.setService(rs.getString(Column.SERVICE));
        baseService.setServiceUa(rs.getString(Column.SERVICE + Column.UA));
        baseService.setPrice(rs.getInt(Column.PRICE));
        return baseService;
    }
}
