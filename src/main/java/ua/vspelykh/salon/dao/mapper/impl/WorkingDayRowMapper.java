package ua.vspelykh.salon.dao.mapper.impl;

import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.WorkingDay;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.dao.mapper.Column.*;

public class WorkingDayRowMapper implements RowMapper<WorkingDay> {

    @Override
    public WorkingDay map(ResultSet rs) throws SQLException {
        WorkingDay workingDay = new WorkingDay();
        workingDay.setId(rs.getInt(ID));
        workingDay.setUserId(rs.getInt(USER_ID));
        workingDay.setDate(rs.getDate(DATE).toLocalDate());
        workingDay.setTimeStart(rs.getTime(TIME_START).toLocalTime());
        workingDay.setTimeEnd(rs.getTime(TIME_END).toLocalTime());
        return workingDay;
    }
}
