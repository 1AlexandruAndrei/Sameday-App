package service;

import config.DatabaseConfiguration;
import exception.InvalidDataException;
import orderInfo.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static final ProductService instance = new ProductService();

    private ProductService() {
    }

    public static ProductService getInstance() {
        return instance;
    }

    public static List<Product> getProductList() {
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

    public void createProduct(String name, double price) throws InvalidDataException {
        if (price < 0) {
            throw new InvalidDataException("Product price cannot be negative.");
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void deleteProductById(int productId) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "DELETE FROM products WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product with ID " + productId + " deleted successfully.");
            } else {
                System.out.println("No product found with ID " + productId + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public static void updateProduct(int productId, String newName, double newPrice) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String updateQuery = "UPDATE products SET name = ?, price = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newName);
            preparedStatement.setDouble(2, newPrice);
            preparedStatement.setInt(3, productId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("No product found with ID " + productId + ". Update failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public static Product getProductById(int productId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Product product = null;

        try {
            connection = DatabaseConfiguration.getConnection();
            String sql = "SELECT * FROM products WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                product = new Product(productId, name, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return product;
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

    public static void displayProductsFromDatabase() {
        List<Product> productList = getProductList();
        System.out.println("Available Products:");
        for (Product product : productList) {
            System.out.println(product.getProductId() + ". " + product.getName() + " ($" + product.getPrice() + ")");
        }
    }
}
