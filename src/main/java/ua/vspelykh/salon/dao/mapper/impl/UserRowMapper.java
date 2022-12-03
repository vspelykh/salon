package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(ID));
        user.setName(rs.getString(NAME));
        user.setSurname(rs.getString(SURNAME));
        user.setEmail(rs.getString(EMAIL));
        user.setNumber(rs.getString(NUMBER));
        user.setPassword(rs.getString(PASSWORD));
        return user;
    }
}
