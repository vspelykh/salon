package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.Appointment;
import ua.vspelykh.salon.model.AppointmentStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class AppointmentRowMapper implements RowMapper<Appointment> {

    @Override
    public Appointment map(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt(ID));
        appointment.setMasterId(rs.getInt(MASTER_ID));
        appointment.setClientId(rs.getInt(CLIENT_ID));
        appointment.setContinuance(rs.getInt(CONTINUANCE));
        appointment.setDate(rs.getTimestamp(DATE).toLocalDateTime());
        appointment.setPrice(rs.getInt(PRICE));
        appointment.setDiscount(rs.getInt(DISCOUNT));
        appointment.setStatus(AppointmentStatus.valueOf(rs.getString(STATUS)));
        return appointment;
    }
}
