package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.UserLevel;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a UserLevel object.
 *
 * @version 1.0
 */
public class UserLevelRowMapper implements RowMapper<UserLevel> {

    /**
     * Maps a single row of a ResultSet to a UserLevel object.
     *
     * @param rs the ResultSet to map from
     * @return a UserLevel object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public UserLevel map(ResultSet rs) throws SQLException {
        return UserLevel.builder()
                .masterId(rs.getInt(ID))
                .level(MastersLevel.valueOf(rs.getString(LEVEL)))
                .about(rs.getString(ABOUT))
                .aboutUa(rs.getString(ABOUT + UA))
                .isActive(rs.getBoolean(ACTIVE))
                .build();
    }
}
