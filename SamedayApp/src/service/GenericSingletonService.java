package service;

import config.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericSingletonService<T> {

    private static Connection connection = DatabaseConfiguration.getConnection();

    protected abstract String getTableName();

    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void setEntityParametersForInsert(PreparedStatement preparedStatement, T entity) throws SQLException;

    protected abstract void setEntityParametersForUpdate(PreparedStatement preparedStatement, T entity) throws SQLException;

    public List<T> getAll() {
        List<T> entities = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + getTableName();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entities.add(mapResultSetToEntity(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public T getById(int id) {
        T entity = null;
        try {
            String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity = mapResultSetToEntity(resultSet);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    public void add(T entity) {
        try {
            String sql = "INSERT INTO " + getTableName() + " VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            setEntityParametersForInsert(statement, entity);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        try {
            String sql = "UPDATE " + getTableName() + " SET name = ?, price = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            setEntityParametersForUpdate(statement, entity);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
