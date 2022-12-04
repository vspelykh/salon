package ua.vspelykh.salon.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.vspelykh.salon.dao.AbstractDao;
import ua.vspelykh.salon.dao.Table;
import ua.vspelykh.salon.dao.UserDao;
import ua.vspelykh.salon.dao.connection.DBCPDataSource;
import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapperFactory;
import ua.vspelykh.salon.model.Role;
import ua.vspelykh.salon.model.User;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {

    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {
        super(DBCPDataSource.getConnection(), RowMapperFactory.getUserRowMapper(), Table.USER);
    }

    @Override
    public User findById(int id) throws DaoException {
        return setUserRoles(super.findById(id));
    }

    @Override
    public int create(User entity) throws DaoException {
        String query = INSERT + tableName + " (name, surname, email, number, password)" + VALUES + "(?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            setUserStatement(entity, statement);
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
    public void update(User entity) throws DaoException {
        String query = "UPDATE users SET name = ?, surname = ?, email = ?, number = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setUserStatement(entity, statement);
            statement.setInt(6, entity.getId());
            int key = statement.executeUpdate();
            if (key != 1) {
                throw new DaoException(FAIL_UPDATE + tableName + ", id=" + entity.getId());
            }
        } catch (SQLException e) {
            LOG.error(FAIL_UPDATE, e);
            throw new DaoException(FAIL_UPDATE + tableName, e);
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws DaoException {
        User user;
        String query = SELECT + tableName + " WHERE email=? AND password=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = rowMapper.map(resultSet);
            } else {
                LOG.error("No entity from {} found by {} with password.", tableName, email);
                throw new DaoException("No entity from " + tableName + " found by " + email + " with password.");
            }
        } catch (SQLException e) {
            LOG.error(FAIL_FIND + tableName +  " by email and password");
            throw new DaoException(FAIL_FIND + tableName +  " by email and password");
        }
        return user;
    }

    @Override
    public User findByNumber(String number) throws DaoException {
        return findByParam(number, Column.NUMBER);
    }

    @Override
    public User findByEmail(String email) throws DaoException {
        return findByParam(email, Column.EMAIL);
    }

    @Override
    public List<User> findClients() throws DaoException {
        return findByRole(Role.CLIENT);
    }

    @Override
    public List<User> findMasters() throws DaoException {
        return findByRole(Role.HAIRDRESSER);
    }

    @Override
    public List<User> findAdministrators() throws DaoException {
        return findByRole(Role.ADMINISTRATOR);
    }

    private List<User> findByRole(Role role) throws DaoException {
        String query = SELECT + "users u INNER JOIN user_roles ur ON u.id = ur.user_id AND ur.role=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = rowMapper.map(resultSet);
                setUserRoles(user);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            LOG.error(e);
            throw new DaoException(e);
        }
    }

    private void setUserStatement(User entity, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, entity.getName());
        statement.setString(++k, entity.getSurname());
        statement.setString(++k, entity.getEmail());
        statement.setString(++k, entity.getNumber());
        statement.setString(++k, entity.getPassword());
    }

    private User setUserRoles(User user) throws DaoException {
        String query = "SELECT role FROM user_roles WHERE user_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            Set<Role> roles = user.getRoles();
            while (resultSet.next()) {
                roles.add(Role.valueOf(resultSet.getString(Column.ROLE)));
            }
            return user;
        } catch (SQLException e) {
            LOG.error("Error during setting roles for user");
            throw new DaoException("Error during setting roles for user", e);
        }
    }
}
