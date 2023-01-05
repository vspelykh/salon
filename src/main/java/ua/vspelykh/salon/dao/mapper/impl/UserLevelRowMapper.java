package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.MastersLevel;
import ua.vspelykh.salon.model.UserLevel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLevelRowMapper implements RowMapper<UserLevel> {
    @Override
    public UserLevel map(ResultSet rs) throws SQLException {
        return new UserLevel(rs.getInt(Column.USER_ID), MastersLevel.valueOf(rs.getString(Column.LEVEL)),
                rs.getString(Column.ABOUT), rs.getBoolean(Column.ACTIVE));
    }
}
