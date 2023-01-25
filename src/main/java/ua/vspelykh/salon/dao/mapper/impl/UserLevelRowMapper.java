package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.model.UserLevel;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class UserLevelRowMapper implements RowMapper<UserLevel> {
    @Override
    public UserLevel map(ResultSet rs) throws SQLException {
        return new UserLevel(rs.getInt(ID), MastersLevel.valueOf(rs.getString(LEVEL)),
                rs.getString(ABOUT), rs.getString(ABOUT + UA), rs.getBoolean(ACTIVE));
    }
}
