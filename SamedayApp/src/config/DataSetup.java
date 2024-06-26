package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSetup {
    public void createTableAndStoredProcedure() throws SQLException {
        String createTableProductsSql = "CREATE TABLE IF NOT EXISTS products " +
                "(id int PRIMARY KEY AUTO_INCREMENT, name varchar(30), " +
                "price double)";

        String deleteProcedureProducts = "DROP PROCEDURE IF EXISTS demo.INSERT_PRODUCTS;";

        String createStoredProcedureProducts = "CREATE PROCEDURE demo.INSERT_PRODUCTS(OUT id int, IN name varchar(30), IN price double) " +
                "BEGIN " +
                "INSERT INTO products(name, price) " +
                "VALUES (name, price); " +
                "SET id = LAST_INSERT_ID(); " +
                "END";

        String createTableLockersSql = "CREATE TABLE IF NOT EXISTS lockers " +
                "(lockerId int PRIMARY KEY AUTO_INCREMENT, location varchar(100), size int, available boolean)";

        String deleteProcedureLockers = "DROP PROCEDURE IF EXISTS demo.INSERT_LOCKERS;";

        String createStoredProcedureLockers = "CREATE PROCEDURE demo.INSERT_LOCKERS(OUT lockerId int, IN location varchar(100), IN size int, IN available boolean) " +
                "BEGIN " +
                "INSERT INTO lockers(location, size, available) " +
                "VALUES (location, size, available); " +
                "SET lockerId = LAST_INSERT_ID(); " +
                "END";

        String createTableUsersSql = "CREATE TABLE IF NOT EXISTS users " +
                "(userId int PRIMARY KEY AUTO_INCREMENT, username varchar(50), " +
                "password varchar(50), email varchar(100), phoneNumber varchar(15))";

        String deleteProcedureUsers = "DROP PROCEDURE IF EXISTS demo.INSERT_USERS;";

        String createStoredProcedureUsers = "CREATE PROCEDURE demo.INSERT_USERS(OUT userId int, IN username varchar(50), IN password varchar(50), IN email varchar(100), IN phoneNumber varchar(15)) " +
                "BEGIN " +
                "INSERT INTO users(username, password, email, phoneNumber) " +
                "VALUES (username, password, email, phoneNumber); " +
                "SET userId = LAST_INSERT_ID(); " +
                "END";

        String createTableWarehousesSql = "CREATE TABLE IF NOT EXISTS warehouses " +
                "(warehouseId int PRIMARY KEY AUTO_INCREMENT, location varchar(100), capacity int)";

        String deleteProcedureWarehouses = "DROP PROCEDURE IF EXISTS demo.INSERT_WAREHOUSES;";

        String createStoredProcedureWarehouses = "CREATE PROCEDURE demo.INSERT_WAREHOUSES(OUT warehouseId int, IN location varchar(100), IN capacity int) " +
                "BEGIN " +
                "INSERT INTO warehouses(location, capacity) " +
                "VALUES (location, capacity); " +
                "SET warehouseId = LAST_INSERT_ID(); " +
                "END";

        String createTableOrdersSql = "CREATE TABLE IF NOT EXISTS orders " +
                "(orderId int PRIMARY KEY AUTO_INCREMENT, userId int, deliveryAddress varchar(255), deliveryTime varchar(50), hasBeenDelivered boolean, feedbackId int, totalOrderValue double, warehouseId int, " +
                "FOREIGN KEY (userId) REFERENCES users(userId), " +
                "FOREIGN KEY (feedbackId) REFERENCES feedback(feedbackId), " +
                "FOREIGN KEY (warehouseId) REFERENCES warehouses(warehouseId))";

        String deleteProcedureOrders = "DROP PROCEDURE IF EXISTS demo.INSERT_ORDERS;";

        String createStoredProcedureOrders = "CREATE PROCEDURE demo.INSERT_ORDERS(OUT orderId int, IN userId int, IN deliveryAddress varchar(255), IN deliveryTime varchar(50), IN hasBeenDelivered boolean, IN feedbackId int, IN totalOrderValue double, IN warehouseId int) " +
                "BEGIN " +
                "INSERT INTO orders(userId, deliveryAddress, deliveryTime, hasBeenDelivered, feedbackId, totalOrderValue, warehouseId) " +
                "VALUES (userId, deliveryAddress, deliveryTime, hasBeenDelivered, feedbackId, totalOrderValue, warehouseId); " +
                "SET orderId = LAST_INSERT_ID(); " +
                "END";

        // Create order_products table (many-to-many relationship)
        String createTableOrderProductsSql = "CREATE TABLE IF NOT EXISTS order_products " +
                "(orderId int, productId int, " +
                "PRIMARY KEY (orderId, productId), " +
                "FOREIGN KEY (orderId) REFERENCES orders(orderId), " +
                "FOREIGN KEY (productId) REFERENCES products(id))";

        String deleteProcedureOrderProducts = "DROP PROCEDURE IF EXISTS demo.INSERT_ORDER_PRODUCTS;";

        String createStoredProcedureOrderProducts = "CREATE PROCEDURE demo.INSERT_ORDER_PRODUCTS(IN orderId int, IN productId int) " +
                "BEGIN " +
                "INSERT INTO order_products(orderId, productId) " +
                "VALUES (orderId, productId); " +
                "END";

        Connection databaseConnection = DatabaseConfiguration.getConnection();
        Statement stmt = databaseConnection.createStatement();

        stmt.execute(createTableProductsSql);
        stmt.execute(deleteProcedureProducts);
        stmt.execute(createStoredProcedureProducts);

        stmt.execute(createTableLockersSql);
        stmt.execute(deleteProcedureLockers);
        stmt.execute(createStoredProcedureLockers);

        stmt.execute(createTableUsersSql);
        stmt.execute(deleteProcedureUsers);
        stmt.execute(createStoredProcedureUsers);

        stmt.execute(createTableWarehousesSql);
        stmt.execute(deleteProcedureWarehouses);
        stmt.execute(createStoredProcedureWarehouses);

        stmt.execute(createTableOrdersSql);
        stmt.execute(deleteProcedureOrders);
        stmt.execute(createStoredProcedureOrders);

        stmt.execute(createTableOrderProductsSql);
        stmt.execute(deleteProcedureOrderProducts);
        stmt.execute(createStoredProcedureOrderProducts);
    }
}
