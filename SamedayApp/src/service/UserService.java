package service;

import config.DatabaseConfiguration;
import exception.InvalidDataException;
import orderInfo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final UserService instance = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return instance;
    }

    public static List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM users";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phoneNumber");
                // Assuming you have a constructor in your User class that accepts these parameters
                userList.add(new User(userId, username, password, email, phoneNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return userList;
    }

    public void createUser(String username, String password, String email, String phoneNumber) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "INSERT INTO users (username, password, email, phoneNumber) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }


    public static void deleteUserById(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "DELETE FROM users WHERE userId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User with ID " + userId + " deleted successfully.");
            } else {
                System.out.println("No user found with ID " + userId + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void updateUser(int userId, String newUsername, String newPassword, String newEmail, String newPhoneNumber) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String updateQuery = "UPDATE users SET username = ?, password = ?, email = ?, phoneNumber = ? WHERE userId = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, newPassword);
            preparedStatement.setString(3, newEmail);
            preparedStatement.setString(4, newPhoneNumber);
            preparedStatement.setInt(5, userId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with ID " + userId + ". Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public static User getUserById(int userId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM users WHERE userId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phoneNumber = resultSet.getString("phoneNumber");
                user = new User(userId, username, password, email, phoneNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return user;
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

    public static void displayUsersFromDatabase() {
        List<User> userList = getUserList();
        System.out.println("User List:");
        for (User user : userList) {
            System.out.println("User ID: " + user.getUserId() + ", Username: " + user.getUsername() +
                    ", Email: " + user.getEmail() + ", Phone Number: " + user.getPhoneNumber());
        }
    }
}
