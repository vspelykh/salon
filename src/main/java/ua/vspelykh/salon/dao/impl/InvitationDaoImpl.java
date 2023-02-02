package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.InvitationDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Invitation;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;

import static ua.vspelykh.salon.dao.mapper.Column.EMAIL;

public class InvitationDaoImpl extends AbstractDao<Invitation> implements InvitationDao {

    private static final Logger LOG = LogManager.getLogger(InvitationDaoImpl.class);

    public InvitationDaoImpl() {
        super(RowMapperFactory.getInvitationRowMapper(), Table.INVITATION);
    }


    @Override
    public int create(Invitation entity) throws DaoException {
        String query = INSERT + tableName + " (email, role, key)" + VALUES + "(?,?,?)";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setInvitationStatement(entity, statement);
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

    private void setInvitationStatement(Invitation entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getEmail());
        statement.setString(++k, entity.getRole().name());
        statement.setString(++k, entity.getKey());
    }

    @Override
    public void update(Invitation entity) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invitation findByEmail(String email) throws DaoException {
        try {
            return findByParam(email, EMAIL);
        } catch (DaoException e) {
            e.printStackTrace();
            //TODO
            throw new DaoException();
        }
    }

    @Override
    public void removeByEmailIfExists(String email) throws DaoException {
        String query = DELETE + tableName + WHERE + EMAIL + EQUAL;
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            int i = statement.executeUpdate();
            if (i != 1) {
                LOG.info("Item for delete didn't find in {}", tableName);
            }
        } catch (SQLException e) {
            LOG.error("Fail to delete item.", e);
            throw new DaoException("Fail to delete item, e");
        }
    }
}
