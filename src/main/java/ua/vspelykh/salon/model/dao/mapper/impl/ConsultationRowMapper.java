package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Consultation;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * * This class implements the RowMapper interface for mapping a ResultSet to a Consultation object.
 *
 * @version 1.0
 */
public class ConsultationRowMapper implements RowMapper<Consultation> {

    /**
     * Maps a single row of a ResultSet to a Consultation object.
     *
     * @param rs the ResultSet to map from
     * @return a Consultation object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public Consultation map(ResultSet rs) throws SQLException {
        return Consultation.builder().id(rs.getInt(ID))
                .name(rs.getString(NAME))
                .number(rs.getString(NUMBER))
                .date(rs.getTimestamp(DATE).toLocalDateTime())
                .build();
    }
}
