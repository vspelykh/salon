package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.WorkingDay;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkingDayRowMapper implements RowMapper<WorkingDay> {

    @Override
    public WorkingDay map(ResultSet rs) throws SQLException {
        WorkingDay workingDay = new WorkingDay();
        workingDay.setId(rs.getInt(Column.ID));
        workingDay.setUserId(rs.getInt(Column.USER_ID));
        workingDay.setDate(rs.getDate(Column.DATE).toLocalDate());
        workingDay.setTimeStart(rs.getTime(Column.TIME_START).toLocalTime());
        workingDay.setTimeEnd(rs.getTime(Column.TIME_END).toLocalTime());
        return workingDay;
    }
}
