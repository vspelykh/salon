package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.UserLevelDao;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserLevelDaoImpl extends AbstractDao<UserLevel> implements UserLevelDao {

    private static final Logger LOG = LogManager.getLogger(UserLevelDaoImpl.class);

    public UserLevelDaoImpl() {
        super(RowMapperFactory.getUserLevelRowMapper(), Table.USER_LEVEL);
    }

    @Override
    public UserLevel findById(int id) throws DaoException {
        return findByParam(id, Column.USER_ID);
    }

    @Override
    public int create(UserLevel entity) throws DaoException {
        String query = INSERT + tableName + " (id, level, active, about)" + VALUES + "(?,?,?,?)";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setStatement(statement, entity);
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

    @Override
    public void update(UserLevel entity) throws DaoException {
        String query = "UPDATE user_level SET level = ? WHERE id = ?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getLevel().toString());
            statement.setInt(2, entity.getMasterId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName);
            }
        } catch (SQLException e) {
            LOG.error(FAIL_UPDATE, e);
            throw new DaoException(FAIL_UPDATE + tableName, e);
        }
    }

    @Override
    public List<User> getUsersByLevel(UserLevel userLevel, boolean isActive) throws DaoException {
        String query = SELECT + "users u INNER JOIN user_level ul ON u.id = ul.id WHERE ul.level=? AND active = ?";
        try (Connection connection = DBCPDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setStatement(statement, userLevel);
            ResultSet resultSet = statement.getGeneratedKeys();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                RowMapperFactory.getUserRowMapper().map(resultSet);
            }
            return users;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    private void setStatement(PreparedStatement statement, UserLevel userLevel) throws SQLException {
        int k = 0;
        statement.setInt(++k, userLevel.getMasterId());
        statement.setString(++k, userLevel.getLevel().name());
        statement.setBoolean(++k, userLevel.isActive());
        statement.setString(++k, userLevel.getAbout());
    }


    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws DaoException {
        return findByParam(userId, Column.USER_ID);
    }

}
