package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.MasterService;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class MasterServiceRowMapper implements RowMapper<MasterService> {
    @Override
    public MasterService map(ResultSet rs) throws SQLException {
        return new MasterService(rs.getInt(ID), rs.getInt(MASTER_ID), rs.getInt(BASE_SERVICE_ID), rs.getInt(CONTINUANCE));
    }
}
