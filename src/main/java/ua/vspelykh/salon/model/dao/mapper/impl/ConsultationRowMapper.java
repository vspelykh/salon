package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Consultation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultationRowMapper implements RowMapper<Consultation> {

    @Override
    public Consultation map(ResultSet rs) throws SQLException {
        return new Consultation(rs.getInt(Column.ID), rs.getString(Column.NAME), rs.getString(Column.NUMBER), rs.getTimestamp(Column.DATE).toLocalDateTime());
    }
}
