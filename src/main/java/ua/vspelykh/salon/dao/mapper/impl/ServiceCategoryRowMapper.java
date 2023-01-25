package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.ServiceCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class ServiceCategoryRowMapper implements RowMapper<ServiceCategory> {

    @Override
    public ServiceCategory map(ResultSet rs) throws SQLException {
        return new ServiceCategory(rs.getInt(ID), rs.getString(NAME), rs.getString(NAME + UA));
    }
}
