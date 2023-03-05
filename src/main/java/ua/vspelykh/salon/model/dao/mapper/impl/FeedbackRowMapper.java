package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Feedback;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a Feedback object.
 *
 * @version 1.0
 */
public class FeedbackRowMapper implements RowMapper<Feedback> {

    /**
     * Maps a single row of a ResultSet to a Feedback object.
     *
     * @param rs the ResultSet to map from
     * @return a Feedback object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public Feedback map(ResultSet rs) throws SQLException {
        return Feedback.builder()
                .id(rs.getInt(ID))
                .appointmentId(rs.getInt(APPOINTMENT_ID))
                .mark(rs.getInt(MARK))
                .comment(rs.getString(COMMENT))
                .date(rs.getTimestamp(DATE).toLocalDateTime())
                .build();
    }
}
