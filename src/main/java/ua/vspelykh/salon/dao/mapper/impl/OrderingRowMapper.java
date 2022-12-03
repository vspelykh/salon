package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.Ordering;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class OrderingRowMapper implements RowMapper<Ordering> {

    @Override
    public Ordering map(ResultSet rs) throws SQLException {
        return new Ordering(rs.getInt(ID), rs.getInt(APPOINTMENT_ID), rs.getInt(SERVICE_ID));
    }
}
