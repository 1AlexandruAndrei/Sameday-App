import config.DatabaseConfiguration;
import Delivery.Locker;
import service.LockerService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    public static void createLockers() {
        LockerService.getInstance().createLocker("Nordului 52", 10, false); // Non-available locker
        LockerService.getInstance().createLocker("Aviatorilor 85", 5, true);  // Available locker
        LockerService.getInstance().createLocker("ONE MAMAIA NORD 2", 9, true);  // Available locker
        saveLockers(LockerService.getInstance().getLockerList()); // Save the lockers to the database
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
                int lockerId = resultSet.getInt("lockerId");
                String location = resultSet.getString("location");
                int size = resultSet.getInt("size");
                boolean available = resultSet.getBoolean("available");
                Locker locker = new Locker(lockerId, location, size, available);
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

    public static void saveLockers(List<Locker> lockerList) {
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
}
