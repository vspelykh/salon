package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentRowMapper implements RowMapper<Appointment> {

    @Override
    public Appointment map(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt(Column.ID));
        appointment.setMasterId(rs.getInt(Column.MASTER_ID));
        appointment.setClientId(rs.getInt(Column.CLIENT_ID));
        appointment.setContinuance(rs.getInt(Column.CONTINUANCE));
        appointment.setDate(rs.getTimestamp(Column.DATE).toLocalDateTime());
        appointment.setPrice(rs.getInt(Column.PRICE));
        appointment.setDiscount(rs.getInt(Column.DISCOUNT));
        appointment.setStatus(AppointmentStatus.valueOf(rs.getString(Column.STATUS)));
        appointment.setPaymentStatus(PaymentStatus.valueOf(rs.getString(Column.PAYMENT_STATUS)));
        return appointment;
    }
}
