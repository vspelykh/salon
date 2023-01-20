package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.Consultation;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class ConsultationRowMapper implements RowMapper<Consultation> {

    @Override
    public Consultation map(ResultSet rs) throws SQLException {
        return new Consultation(rs.getInt(ID), rs.getString(NAME), rs.getString(NUMBER), rs.getTimestamp(DATE).toLocalDateTime());
    }
}
