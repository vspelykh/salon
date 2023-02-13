package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.ConsultationDao;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.Consultation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultationDaoImpl extends AbstractDao<Consultation> implements ConsultationDao {

    private static final Logger LOG = LogManager.getLogger(ConsultationDaoImpl.class);

    public ConsultationDaoImpl() {
        super(RowMapperFactory.getConsultationRowMapper(), Table.CONSULTATION);
    }

    @Override
    public int create(Consultation entity) throws DaoException {
        String query = INSERT + tableName + " (name, number)" + VALUES + "(?,?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(entity, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException(NO_ID + tableName);
            }
        } catch (SQLException e) {
            LOG.error(String.format(LOG_PATTERN, FAIL_CREATE, tableName, e.getMessage()));
            throw new DaoException(e);
        }
    }

    private void setStatement(Consultation entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getNumber());
    }

    @Override
    public void update(Consultation entity) throws DaoException {
        throw new UnsupportedOperationException();
    }
}
