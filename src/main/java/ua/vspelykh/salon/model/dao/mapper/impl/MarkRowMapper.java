package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Feedback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarkRowMapper implements RowMapper<Feedback> {
    @Override
    public Feedback map(ResultSet rs) throws SQLException {
        Feedback mark = new Feedback();
        mark.setId(rs.getInt(Column.ID));
        mark.setAppointmentId(rs.getInt(Column.APPOINTMENT_ID));
        mark.setMark(rs.getInt(Column.MARK));
        mark.setComment(rs.getString(Column.COMMENT));
        mark.setDate(rs.getTimestamp(Column.DATE).toLocalDateTime());
        return mark;
    }
}
