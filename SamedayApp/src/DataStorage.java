import Delivery.Locker;
import Delivery.Warehouse;
import config.DatabaseConfiguration;
import exception.InvalidDataException;
import orderInfo.Product;
import service.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataStorage {

    public static void createProducts() {
        try {
            ProductService.getInstance().createProduct("T-shirt", 50);
            ProductService.getInstance().createProduct("iPhone 15 Pro Max", 1000);
            ProductService.getInstance().createProduct("Lamborghini Aventador", 100000);
            ProductService.getInstance().createProduct("Sprite", 7.5);
            ProductService.getInstance().createProduct("Jeans", 200);
            ProductService.getInstance().createProduct("PC", 10000);
            ProductService.getInstance().createProduct("Dom Perignon", 1250.5);
            ProductService.getInstance().createProduct("Cartea 'Felicitari'", 100);
        } catch (InvalidDataException e) {
            e.printStackTrace();
            // Handle the exception here, such as logging or displaying an error message
        }
    }


    public static void addLocker() {
        LockerService.getInstance().addLocker(10, false, "Nordului 52"); // Non-available locker
        LockerService.getInstance().addLocker(5, true, "Aviatorilor 85");   // Available locker
        LockerService.getInstance().addLocker(9, true, "ONE MAMAIA NORD 2");   // Available locker
        saveLockers(LockerService.getInstance().getLockerList()); // Save the lockers to the database
    }

    public static void createWarehouses() {
        Warehouse warehouse1 = new Warehouse(1, "Militari Petrom", 10000);
        WarehouseService.getInstance().addWarehouse(warehouse1.getLocation(), warehouse1.getCapacity());

        Warehouse warehouse2 = new Warehouse(2, "Nordului Nowa", 5);
        WarehouseService.getInstance().addWarehouse(warehouse2.getLocation(), warehouse2.getCapacity());
    }

    public static void displayAll() {
        UserService.getInstance().displayUser(UserService.getInstance().getUserList().get(0));
        FeedbackService.getInstance().displayFeedbackForOrder(1);
        FeedbackService.getInstance().displayFeedbackForOrder(2);
    }

    public static List<Product> fetchProductsFromDatabase() {
        List<Product> productList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM products";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                Product product = new Product(name, price);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return productList;
    }

    public static void addProduct(String productName, double productPrice) throws SQLException {
        Connection connection = DatabaseConfiguration.getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, productName);
            statement.setDouble(2, productPrice);
            statement.executeUpdate();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static List<Locker> fetchLockersFromDatabase() {
        List<Locker> lockerList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM lockers");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String location = resultSet.getString("location");
                int size = resultSet.getInt("size");
                boolean available = resultSet.getBoolean("available");
                Locker locker = new Locker(id, location, size, available);
                lockerList.add(locker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return lockerList;
    }

    public static void addLocker(String location, int size, boolean available) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "INSERT INTO lockers (location, size, available) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, location);
            statement.setInt(2, size);
            statement.setBoolean(3, available);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void saveLockers(List<Locker> lockerList) {
        Connection connection = null;
        PreparedStatement deleteStatement = null;
        PreparedStatement insertStatement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            deleteStatement = connection.prepareStatement("DELETE FROM lockers");
            deleteStatement.executeUpdate();

            insertStatement = connection.prepareStatement("INSERT INTO lockers (location, size, available) VALUES (?, ?, ?)");
            for (Locker locker : lockerList) {
                insertStatement.setString(1, locker.getLocation());
                insertStatement.setInt(2, locker.getSize());
                insertStatement.setBoolean(3, locker.isAvailable());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (deleteStatement != null) deleteStatement.close();
                if (insertStatement != null) insertStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}