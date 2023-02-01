package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.Invitation;
import ua.vspelykh.salon.model.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class InvitationRowMapper implements RowMapper<Invitation> {
    @Override
    public Invitation map(ResultSet rs) throws SQLException {
        return new Invitation(rs.getInt(ID), rs.getString(EMAIL), Role.valueOf(rs.getString(ROLE)),
                rs.getString(KEY), rs.getDate(DATE).toLocalDate());
    }
}
