package ua.vspelykh.salon.dao.impl;

import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.UserLevelDao;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.model.UserLevel;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserLevelDaoImpl extends AbstractDao<UserLevel> implements UserLevelDao {

    public UserLevelDaoImpl() {
        super(DBCPDataSource.getConnection(), RowMapperFactory.getUserLevelRowMapper(), Table.USER_LEVEL);
    }

    @Override
    public UserLevel findById(int id) throws DaoException {
        return findByParam(id, Column.USER_ID);
    }

    @Override
    public int create(UserLevel entity) throws DaoException {
        String query = "INSERT INTO " + tableName + " (user_id, level)" + " VALUES " + "(?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DaoException("Fail to create UserLevel");
            }
        } catch (SQLException e) {
//            getLOG().error("Fail to insert item", e);
            throw new DaoException("Fail to insert UserLevel", e);
        }
    }

    @Override
    public void update(UserLevel entity) throws DaoException {
        String query = "UPDATE user_level SET level = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getLevel().toString());
            statement.setInt(2, entity.getMasterId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> getUsersByLevel(UserLevel userLevel) throws DaoException {
        String query = "SELECT * FROM users u INNER JOIN user_level ul ON u.id = ul.user_id WHERE ul.level=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userLevel.getLevel().name());
            ResultSet resultSet = statement.getGeneratedKeys();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                RowMapperFactory.getUserRowMapper().map(resultSet);
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public UserLevel getUserLevelByUserId(Integer userId) throws DaoException {
        return findByParam(userId, Column.USER_ID);
    }

}
