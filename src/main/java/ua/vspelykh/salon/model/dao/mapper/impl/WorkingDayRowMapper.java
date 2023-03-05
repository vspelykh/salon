package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.WorkingDay;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a WorkingDay object.
 *
 * @version 1.0
 */
public class WorkingDayRowMapper implements RowMapper<WorkingDay> {

    /**
     * Maps the current row in the ResultSet to a WorkingDay object.
     *
     * @param rs the ResultSet containing the data to be mapped
     * @return the mapped WorkingDay object
     * @throws SQLException if a database access error occurs or if the column label is not valid
     */
    @Override
    public WorkingDay map(ResultSet rs) throws SQLException {
        return WorkingDay.builder()
                .id(rs.getInt(ID))
                .userId(rs.getInt(USER_ID))
                .date(rs.getDate(DATE).toLocalDate())
                .timeStart(rs.getTime(TIME_START).toLocalTime())
                .timeEnd(rs.getTime(TIME_END).toLocalTime())
                .build();
    }
}
