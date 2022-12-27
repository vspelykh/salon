package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.MarkDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Mark;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarkDaoImpl extends AbstractDao<Mark> implements MarkDao {

    private static final Logger LOG = LogManager.getLogger(MarkDaoImpl.class);

    public MarkDaoImpl() {
        super(RowMapperFactory.getMarkRowMapper(), Table.MARK);
    }

    @Override
    public int create(Mark entity) throws DaoException {
        String query = INSERT + tableName + " (appointment_id, mark, comment, date)" + VALUES + "(?,?,?,?)";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setMarkStatement(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException(NO_ID + tableName);
            }
        } catch (SQLException e) {
            LOG.error(FAIL_CREATE + tableName, e);
            throw new DaoException(FAIL_CREATE + tableName, e);
        }
    }

    private void setMarkStatement(Mark entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setInt(++k, entity.getAppointmentId());
        statement.setInt(++k, entity.getMark());
        statement.setString(++k, entity.getComment());
        statement.setTimestamp(++k, Timestamp.valueOf(entity.getDate()));
    }

    @Override
    public void update(Mark entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Mark> getMarksByMasterId(Integer masterId) throws DaoException {
        String query = SELECT + tableName + " WHERE appointment_id IN(SELECT id FROM appointments WHERE master_id=?)";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, masterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Mark> marks = new ArrayList<>();
            while (resultSet.next()) {
                Mark entity = rowMapper.map(resultSet);
                marks.add(entity);
            }
            return marks;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }
}
