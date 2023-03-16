package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Appointment;
import ua.vspelykh.salon.model.entity.AppointmentStatus;
import ua.vspelykh.salon.model.entity.PaymentStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AppointmentRowMapper is a class that implements the RowMapper interface for mapping a single row of
 * a ResultSet to an Appointment object.
 *
 * @version 1.0
 */
public class AppointmentRowMapper implements RowMapper<Appointment> {

    /**
     * This class implements the RowMapper interface for mapping a ResultSet to an Appointment object.
     *
     * @param rs the ResultSet to map from
     * @return an Appointment object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public Appointment map(ResultSet rs) throws SQLException {
        return Appointment.builder().
                id(rs.getInt(Column.ID))
                .masterId(rs.getInt(Column.MASTER_ID))
                .clientId(rs.getInt(Column.CLIENT_ID))
                .continuance(rs.getInt(Column.CONTINUANCE))
                .date(rs.getTimestamp(Column.DATE).toLocalDateTime())
                .price(rs.getInt(Column.PRICE))
                .discount(rs.getInt(Column.DISCOUNT))
                .status(AppointmentStatus.valueOf(rs.getString(Column.STATUS)))
                .paymentStatus(PaymentStatus.valueOf(rs.getString(Column.PAYMENT_STATUS)))
                .build();
    }
}
