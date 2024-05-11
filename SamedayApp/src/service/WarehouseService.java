package service;
import config.DatabaseConfiguration;
import Delivery.Warehouse;
import orderInfo.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WarehouseService {
    private static final WarehouseService instance = new WarehouseService();

    private WarehouseService() {
    }

    public static WarehouseService getInstance() {
        return instance;
    }

    public static List<Warehouse> getWarehouseList() {
        List<Warehouse> warehouseList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM warehouses";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int warehouseId = resultSet.getInt("warehouseId");
                String location = resultSet.getString("location");
                int capacity = resultSet.getInt("capacity");
                warehouseList.add(new Warehouse(warehouseId, location, capacity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return warehouseList;
    }

    public void createWarehouse(String location, int capacity) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "INSERT INTO warehouses (location, capacity) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, location);
            statement.setInt(2, capacity);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void deleteWarehouseById(int warehouseId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "DELETE FROM warehouses WHERE warehouseId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, warehouseId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Warehouse with ID " + warehouseId + " deleted successfully.");
            } else {
                System.out.println("No warehouse found with ID " + warehouseId + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void updateWarehouse(int warehouseId, String newLocation, int newCapacity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String updateQuery = "UPDATE warehouses SET location = ?, capacity = ? WHERE warehouseId = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newLocation);
            preparedStatement.setInt(2, newCapacity);
            preparedStatement.setInt(3, warehouseId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Warehouse updated successfully.");
            } else {
                System.out.println("No warehouse found with ID " + warehouseId + ". Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public static Warehouse getWarehouseById(int warehouseId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Warehouse warehouse = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM warehouses WHERE warehouseId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, warehouseId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String location = resultSet.getString("location");
                int capacity = resultSet.getInt("capacity");
                warehouse = new Warehouse(warehouseId, location, capacity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return warehouse;
    }

    private static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayWarehousesFromDatabase() {
        List<Warehouse> warehouseList = getWarehouseList();
        System.out.println("Available Warehouses:");
        for (Warehouse warehouse : warehouseList) {
            System.out.println(warehouse.getWarehouseId() + ". " + warehouse.getLocation() + " (Capacity: " + warehouse.getCapacity() + ")");
        }
    }
}
