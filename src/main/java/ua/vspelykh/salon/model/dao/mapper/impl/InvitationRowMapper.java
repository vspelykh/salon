package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.model.entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to an Invitation object.
 *
 * @version 1.0
 */
public class InvitationRowMapper implements RowMapper<Invitation> {

    /**
     * Maps a single row of a ResultSet to an Invitation object.
     *
     * @param rs the ResultSet to map from
     * @return an Invitation object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public Invitation map(ResultSet rs) throws SQLException {
        return Invitation.builder()
                .id(rs.getInt(ID))
                .email(rs.getString(EMAIL))
                .role(Role.valueOf(rs.getString(ROLE)))
                .key(rs.getString(KEY))
                .date(rs.getDate(DATE).toLocalDate())
                .build();
    }
}
