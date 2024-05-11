package service;

import config.DatabaseConfiguration;
import exception.InvalidDataException;
import Delivery.Locker;
import orderInfo.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LockerService {
    private static final LockerService instance = new LockerService();

    private LockerService() {
    }

    public static LockerService getInstance() {
        return instance;
    }

    public static List<Locker> getLockerList() {
        List<Locker> lockerList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM lockers";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int lockerId = resultSet.getInt("lockerId");
                String location = resultSet.getString("location");
                int size = resultSet.getInt("size");
                boolean available = resultSet.getBoolean("available");
                lockerList.add(new Locker(lockerId, location, size, available));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return lockerList;
    }

    public void createLocker(String location, int size, boolean available) {
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
            closeResources(connection, statement, null);
        }
    }

    public static void deleteLockerById(int lockerId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "DELETE FROM lockers WHERE lockerId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, lockerId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Locker with ID " + lockerId + " deleted successfully.");
            } else {
                System.out.println("No locker found with ID " + lockerId + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void updateLocker(int lockerId, String newLocation, int newSize, boolean newAvailability) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String updateQuery = "UPDATE lockers SET location = ?, size = ?, available = ? WHERE lockerId = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newLocation);
            preparedStatement.setInt(2, newSize);
            preparedStatement.setBoolean(3, newAvailability);
            preparedStatement.setInt(4, lockerId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Locker updated successfully.");
            } else {
                System.out.println("No locker found with ID " + lockerId + ". Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public static Locker getLockerById(int lockerId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Locker locker = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM lockers WHERE lockerId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, lockerId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String location = resultSet.getString("location");
                int size = resultSet.getInt("size");
                boolean available = resultSet.getBoolean("available");
                locker = new Locker(lockerId, location, size, available);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return locker;
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

    public static void displayLockersFromDatabase() {
        List<Locker> lockerList = getLockerList();
        System.out.println("Available Lockers:");
        for (Locker locker : lockerList) {
            System.out.println(locker.getLockerId() + ". " + locker.getLocation() + " (Size: " + locker.getSize() + ", Available: " + locker.isAvailable() + ")");
        }
    }
    public void addOrderToLocker(Order order) {
        List<Locker> lockers = getLockerList();
        for (Locker locker : lockers) {
            if (locker.isAvailable()) {
                locker.setAvailable(false);
                //order.setLockerId(locker.getLockerId()); // Update the order with the locker ID
                order.setHasBeenDelivered(false); // Assuming false means not delivered
                System.out.println("Pick up the order from the locker situated at " + locker.getLocation());
                saveLocker(locker);
                break;
            }
        }
    }

    public void saveLocker(Locker locker) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "UPDATE lockers SET available = ? WHERE lockerId = ?";
            statement = connection.prepareStatement(sql);
            statement.setBoolean(1, locker.isAvailable());
            statement.setInt(2, locker.getLockerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }
}
