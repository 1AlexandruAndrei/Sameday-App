package service;

import config.DatabaseConfiguration;
import orderInfo.Order;
import orderInfo.PremiumUser;
import orderInfo.Product;
import orderInfo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderService {
    private static OrderService instance;
    protected static final Scanner scanner = new Scanner(System.in);

    private OrderService() {}

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public void createOrderInDatabase(User user, List<Product> selectedProducts, String deliveryAddress, String deliveryTime, int feedbackId) {
        if (selectedProducts.isEmpty()) {
            System.out.println("NO PRODUCTS SELECTED. THE ORDER WAS NOT CREATED.");
            return;
        }

        User retrievedUser = UserService.getInstance().getUserById(user.getUserId());
        if (retrievedUser != null) {
            double totalOrderValue = 0.0;

            for (Product product : selectedProducts) {
                totalOrderValue += product.getPrice();
            }

            if (retrievedUser instanceof PremiumUser) {
                totalOrderValue = totalOrderValue * 0.9; // 10% discount
            }

            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = DatabaseConfiguration.getConnection();
                String sql = "INSERT INTO orders (userId, deliveryAddress, deliveryTime, hasBeenDelivered, feedbackId, totalOrderValue, warehouseId) VALUES (?, ?, ?, ?, ?, ?, ?)";
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, retrievedUser.getUserId());
                statement.setString(2, deliveryAddress);
                statement.setString(3, deliveryTime);
                statement.setBoolean(4, false);
                statement.setInt(5, feedbackId);
                statement.setDouble(6, totalOrderValue);
                statement.setNull(7, Types.INTEGER);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    Order newOrder = new Order(orderId, retrievedUser, selectedProducts, deliveryAddress, deliveryTime, false, null, totalOrderValue, null);
                    for (Product product : selectedProducts) {
                        insertOrderProduct(orderId, product.getProductId());
                    }
                    LockerService.getInstance().addOrderToLocker(newOrder);
                    System.out.println("\nOrder created successfully. \n-----------------------------------");
                    System.out.println("Dear " + retrievedUser.getUsername() + ", thank you for your order.\n");
                    System.out.println("Your order value is $" + totalOrderValue);
                    System.out.println("\nYour order will be delivered to " + deliveryAddress);
                    System.out.println("\nWe hope to deliver the order by " + deliveryTime);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeResources(connection, statement, null);
            }
        } else {
            System.out.println("USER NOT FOUND. TRY AGAIN!.");
        }
    }

    public static void deleteOrderById(int orderId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "DELETE FROM orders WHERE orderId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Order deleted successfully.");
            } else {
                System.out.println("No order found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void updateOrder(Order order) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String updateQuery = "UPDATE orders SET userId = ?, deliveryAddress = ?, deliveryTime = ?, hasBeenDelivered = ?, feedbackId = ?, totalOrderValue = ?, warehouseId = ? WHERE orderId = ?";
            statement = connection.prepareStatement(updateQuery);
            statement.setInt(1, order.getUser().getUserId());
            statement.setString(2, order.getDeliveryAddress());
            statement.setString(3, order.getDeliveryTime());
            statement.setBoolean(4, order.isHasBeenDelivered());
            statement.setInt(5, order.getFeedback() != null ? order.getFeedback().getFeedbackId() : null);
            statement.setDouble(6, order.getTotalOrderValue());
            statement.setInt(7, order.getWarehouse() != null ? order.getWarehouse().getWarehouseId() : null);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Order updated successfully.");
            } else {
                System.out.println("No order found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static Order getOrderByIdFromDatabase(int orderId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Order order = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM orders WHERE orderId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String deliveryAddress = resultSet.getString("deliveryAddress");
                String deliveryTime = resultSet.getString("deliveryTime");
                boolean hasBeenDelivered = resultSet.getBoolean("hasBeenDelivered");
                int feedbackId = resultSet.getInt("feedbackId");
                double totalOrderValue = resultSet.getDouble("totalOrderValue");
                int warehouseId = resultSet.getInt("warehouseId");

                User user = UserService.getInstance().getUserById(userId);
                List<Product> products = getProductListForOrder(orderId);


                order = new Order(orderId, user, products, deliveryAddress, deliveryTime, hasBeenDelivered, null, totalOrderValue, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return order;
    }

    private static List<Product> getProductListForOrder(int orderId) {
        List<Product> productList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT p.* FROM products p INNER JOIN order_products op ON p.id = op.productId WHERE op.orderId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                productList.add(new Product(productId, name, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return productList;
    }

    private static void insertOrderProduct(int orderId, int productId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "INSERT INTO order_products (orderId, productId) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
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
}
