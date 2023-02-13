package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.Invitation;
import ua.vspelykh.salon.model.entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvitationRowMapper implements RowMapper<Invitation> {
    @Override
    public Invitation map(ResultSet rs) throws SQLException {
        return new Invitation(rs.getInt(Column.ID), rs.getString(Column.EMAIL), Role.valueOf(rs.getString(Column.ROLE)),
                rs.getString(Column.KEY), rs.getDate(Column.DATE).toLocalDate());
    }
}
