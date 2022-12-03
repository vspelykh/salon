package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class MasterServiceRowMapper implements RowMapper<Service> {
    @Override
    public Service map(ResultSet rs) throws SQLException {
        return new Service(rs.getInt(ID), rs.getInt(MASTER_ID), rs.getInt(BASE_SERVICE_ID), rs.getInt(CONTINUANCE));
    }
}
