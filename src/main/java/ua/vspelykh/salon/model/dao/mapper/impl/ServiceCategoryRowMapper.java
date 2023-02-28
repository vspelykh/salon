package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.ServiceCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceCategoryRowMapper implements RowMapper<ServiceCategory> {

    @Override
    public ServiceCategory map(ResultSet rs) throws SQLException {
        return new ServiceCategory(rs.getInt(Column.ID), rs.getString(Column.NAME), rs.getString(Column.NAME + Column.UA));
    }
}
