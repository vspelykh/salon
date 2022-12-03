package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.Mark;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class MarkRowMapper implements RowMapper<Mark> {
    @Override
    public Mark map(ResultSet rs) throws SQLException {
        Mark mark = new Mark();
        mark.setId(rs.getInt(ID));
        mark.setAppointmentId(rs.getInt(APPOINTMENT_ID));
        mark.setMark(rs.getInt(MARK));
        mark.setComment(rs.getString(COMMENT));
        mark.setDate(rs.getTimestamp(DATE).toLocalDateTime());
        return mark;
    }
}
