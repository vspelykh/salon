package ua.vspelykh.salon.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A RowMapper interface is used to map each row of a ResultSet to an object of type T.
 *
 * @param <T> the type of object that each row of the ResultSet should be mapped to.
 * @version 1.0
 */
public interface RowMapper<T> {

    /**
     * Maps a single row of a ResultSet to an object of type T.
     *
     * @param rs the ResultSet to map from
     * @return an object of type T that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    T map(ResultSet rs) throws SQLException;
}
