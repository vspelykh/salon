package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.AppointmentItem;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to an AppointmentItem object.
 *
 * @version 1.0
 */
public class AppointmentItemRowMapper implements RowMapper<AppointmentItem> {

    /**
     * Maps a single row of a ResultSet to an AppointmentItem object.
     *
     * @param rs the ResultSet to map from
     * @return an AppointmentItem object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public AppointmentItem map(ResultSet rs) throws SQLException {
        return new AppointmentItem(rs.getInt(Column.ID), rs.getInt(Column.APPOINTMENT_ID), rs.getInt(Column.SERVICE_ID));
    }
}
