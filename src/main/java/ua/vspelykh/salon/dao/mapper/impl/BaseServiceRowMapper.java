package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.BaseService;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class BaseServiceRowMapper implements RowMapper<BaseService> {

    @Override
    public BaseService map(ResultSet rs) throws SQLException {
        return new BaseService(rs.getInt(ID), rs.getString(SERVICE), rs.getInt(PRICE));
    }
}
