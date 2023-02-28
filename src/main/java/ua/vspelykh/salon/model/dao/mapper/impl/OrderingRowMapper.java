package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.AppointmentItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderingRowMapper implements RowMapper<AppointmentItem> {

    @Override
    public AppointmentItem map(ResultSet rs) throws SQLException {
        return new AppointmentItem(rs.getInt(Column.ID), rs.getInt(Column.APPOINTMENT_ID), rs.getInt(Column.SERVICE_ID));
    }
}
