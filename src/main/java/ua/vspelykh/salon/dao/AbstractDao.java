package ua.vspelykh.salon.dao;

import ua.vspelykh.salon.dao.mapper.Column;
import ua.vspelykh.salon.dao.mapper.RowMapper;
import ua.vspelykh.salon.util.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ua.vspelykh.salon.dao.connection.DBCPDataSource.getConnection;

public abstract class AbstractDao<T> implements Dao<T> {

    protected Connection connection;
    protected RowMapper<T> rowMapper;
    protected final String tableName;

    protected AbstractDao(Connection connection, RowMapper<T> rowMapper, String tableName) {
        this.connection = connection;
        this.rowMapper = rowMapper;
        this.tableName = tableName;
    }

    @Override
    public T findById(int id) throws DaoException {
        return findByParam(id, Column.ID);
    }

    @Override
    public List<T> findAll() throws DaoException {
        String query = "SELECT * FROM " + tableName;
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<T> entities = new ArrayList<>();
            while (resultSet.next()) {
                T entity = rowMapper.map(resultSet);
                entities.add(entity);
            }
            return entities;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void removeById(int id) throws DaoException {
        String query = "DELETE FROM " + tableName + " WHERE " + Column.ID + "=?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected T findByParam(Object value, String param) throws DaoException {
        T entity = null;
        String query = "SELECT * FROM " + tableName + " WHERE " + param + "=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entity = rowMapper.map(resultSet);
            } else {
                //log
                throw new DaoException();
            }
        } catch (SQLException e) {

            throw new DaoException();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return entity;
    }

    protected List<T> findAllByParam(Object value, String param) throws DaoException {
        List<T> entities = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE " + param + "=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                entities.add(rowMapper.map(resultSet));
            } else {
                //log
                throw new DaoException();
            }
        } catch (SQLException e) {

            throw new DaoException();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return entities;
    }
}
