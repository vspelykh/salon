package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(Column.ID));
        user.setName(rs.getString(Column.NAME));
        user.setSurname(rs.getString(Column.SURNAME));
        user.setEmail(rs.getString(Column.EMAIL));
        user.setNumber(rs.getString(Column.NUMBER));
        user.setPassword(rs.getString(Column.PASSWORD));
        return user;
    }
}
