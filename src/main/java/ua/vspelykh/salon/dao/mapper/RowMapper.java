package ua.vspelykh.salon.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {

    T map(ResultSet rs) throws SQLException;
}
