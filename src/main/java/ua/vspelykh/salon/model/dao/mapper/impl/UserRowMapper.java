package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a User object.
 *
 * @version 1.0
 */
public class UserRowMapper implements RowMapper<User> {

    /**
     * Maps a single row of a ResultSet to a UserLevel object.
     *
     * @param rs the ResultSet to map from
     * @return a UserLevel object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public User map(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt(ID))
                .name(rs.getString(NAME))
                .surname(rs.getString(SURNAME))
                .email(rs.getString(EMAIL))
                .number(rs.getString(NUMBER))
                .birthday(LocalDate.parse(rs.getString(BIRTHDAY)))
                .password(rs.getString(PASSWORD))
                .roles(new HashSet<>())
                .build();
    }
}
