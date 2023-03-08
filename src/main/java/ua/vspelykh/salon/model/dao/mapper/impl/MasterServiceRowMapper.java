package ua.vspelykh.salon.model.dao.mapper.impl;

import ua.vspelykh.salon.model.dao.mapper.RowMapper;
import ua.vspelykh.salon.model.entity.MasterService;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.vspelykh.salon.model.dao.mapper.Column.*;

/**
 * This class implements the RowMapper interface for mapping a ResultSet to a MasterService object.
 *
 * @version 1.0
 */
public class MasterServiceRowMapper implements RowMapper<MasterService> {

    /**
     * Maps a single row of a ResultSet to a MasterService object.
     *
     * @param rs the ResultSet to map from
     * @return a MasterService object that represents the data in the row of the ResultSet
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    @Override
    public MasterService map(ResultSet rs) throws SQLException {
        return MasterService.builder()
                .id(rs.getInt(ID))
                .masterId(rs.getInt(MASTER_ID))
                .baseServiceId(rs.getInt(BASE_SERVICE_ID))
                .continuance(rs.getInt(CONTINUANCE))
                .build();
    }
}
