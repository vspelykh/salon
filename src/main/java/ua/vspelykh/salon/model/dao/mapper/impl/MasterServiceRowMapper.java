package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.MasterService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MasterServiceRowMapper implements RowMapper<MasterService> {
    @Override
    public MasterService map(ResultSet rs) throws SQLException {
        return new MasterService(rs.getInt(Column.ID), rs.getInt(Column.MASTER_ID), rs.getInt(Column.BASE_SERVICE_ID), rs.getInt(Column.CONTINUANCE));
    }
}
