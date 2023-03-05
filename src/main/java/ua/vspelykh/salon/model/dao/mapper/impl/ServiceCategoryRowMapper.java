package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.ServiceCategory;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a ServiceCategory object.
 *
 * @version 1.0
 */
public class ServiceCategoryRowMapper implements RowMapper<ServiceCategory> {

    /**
     * Maps a single row of a ResultSet to a ServiceCategory object.
     *
     * @param rs the ResultSet to map from
     * @return a ServiceCategory object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public ServiceCategory map(ResultSet rs) throws SQLException {
        return ServiceCategory.builder()
                .id(rs.getInt(ID))
                .name(rs.getString(NAME))
                .nameUa(rs.getString(NAME + UA))
                .build();
    }
}
