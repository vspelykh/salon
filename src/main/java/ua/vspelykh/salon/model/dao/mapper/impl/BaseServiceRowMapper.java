package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.BaseService;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a BaseService object.
 *
 * @version 1.0
 */
public class BaseServiceRowMapper implements RowMapper<BaseService> {

    /**
     * Maps a single row of a ResultSet to a BaseService object.
     *
     * @param rs the ResultSet to map from
     * @return a BaseService object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public BaseService map(ResultSet rs) throws SQLException {
        return BaseService.builder()
                .id(rs.getInt(ID))
                .categoryId(rs.getInt(CATEGORY_ID))
                .service(rs.getString(SERVICE))
                .serviceUa(rs.getString(SERVICE + UA))
                .price(rs.getInt(PRICE))
                .build();
    }
}
