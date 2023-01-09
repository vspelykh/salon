package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.ConsultationDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Consultation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;

public class ConsultationDaoImpl extends AbstractDao<Consultation> implements ConsultationDao {

    private static final Logger LOG = LogManager.getLogger(ConsultationDaoImpl.class);

    public ConsultationDaoImpl() {
        super(RowMapperFactory.getConsultationRowMapper(), Table.CONSULTATION);
    }


    @Override
    public int create(Consultation entity) throws DaoException {
        String query = INSERT + tableName + " (name, number)" + VALUES + "(?,?)";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(entity, statement);
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

    private void setStatement(Consultation entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getNumber());
//        statement.setTimestamp(++k, Timestamp.valueOf(entity.getDate()));
    }

    @Override
    public void update(Consultation entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
