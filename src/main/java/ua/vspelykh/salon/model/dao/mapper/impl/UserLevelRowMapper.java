package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.MastersLevel;
import ua.vspelykh.salon.model.entity.UserLevel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLevelRowMapper implements RowMapper<UserLevel> {
    @Override
    public UserLevel map(ResultSet rs) throws SQLException {
        return new UserLevel(rs.getInt(Column.ID), MastersLevel.valueOf(rs.getString(Column.LEVEL)),
                rs.getString(Column.ABOUT), rs.getString(Column.ABOUT + Column.UA), rs.getBoolean(Column.ACTIVE));
    }
}
