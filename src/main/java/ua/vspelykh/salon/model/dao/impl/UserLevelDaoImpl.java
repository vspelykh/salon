package ua.vspelykh.salon.model.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.model.dao.AbstractDao;
import ua.vspelykh.salon.model.dao.Table;
import ua.vspelykh.salon.model.dao.UserLevelDao;
import ua.vspelykh.salon.model.dao.mapper.Column;
import ua.vspelykh.salon.model.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.entity.User;
import ua.vspelykh.salon.model.entity.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        String query = INSERT + tableName + " (id, level, active, about, about_ua)" + VALUES + "(?,?,?,?,?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
        String query = "UPDATE user_level SET level=?, about=?, about_ua=? WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            int k = 0;
            statement.setString(++k, entity.getLevel().toString());
            statement.setString(++k, entity.getAbout());
            statement.setString(++k, entity.getAboutUa());
            statement.setInt(++k, entity.getMasterId());
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
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
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
        statement.setString(++k, userLevel.getAboutUa());
    }


    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws DaoException {
        return findByParam(userId, Column.ID);
    }

}
