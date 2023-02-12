package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.AppointmentItem;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class OrderingRowMapper implements RowMapper<AppointmentItem> {

    @Override
    public AppointmentItem map(ResultSet rs) throws SQLException {
        return new AppointmentItem(rs.getInt(ID), rs.getInt(APPOINTMENT_ID), rs.getInt(SERVICE_ID));
    }
}
