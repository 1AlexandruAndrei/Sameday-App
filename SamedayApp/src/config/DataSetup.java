package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSetup {
    public void createTableAndStoredProcedure() throws SQLException {
        String createTableProductsSql = "CREATE TABLE IF NOT EXISTS products" +
                "(id int PRIMARY KEY AUTO_INCREMENT, name varchar(30)," +
                "price double)";

        String deleteProcedureProducts = "DROP PROCEDURE IF EXISTS demo.INSERT_PRODUCTS;";

        String createStoredProcedureProducts = "CREATE PROCEDURE IF NOT EXISTS INSERT_PRODUCTS(OUT id int, IN name varchar(30), IN price double) " +
                "BEGIN " +
                "INSERT INTO products(name,price) " +
                "VALUES (name,price); " +
                "SET id = LAST_INSERT_ID(); " +
                "END";

        String createTableLockersSql = "CREATE TABLE IF NOT EXISTS lockers" +
                "(lockerId int PRIMARY KEY AUTO_INCREMENT, location varchar(100), size int, available boolean)";

        String deleteProcedureLockers = "DROP PROCEDURE IF EXISTS demo.INSERT_LOCKERS;";

        String createStoredProcedureLockers = "CREATE PROCEDURE IF NOT EXISTS INSERT_LOCKERS(OUT lockerId int, IN location varchar(100), IN size int, IN available boolean) " +
                "BEGIN " +
                "INSERT INTO lockers(location, size, available) " +
                "VALUES (location, size, available); " +
                "SET lockerId = LAST_INSERT_ID(); " +
                "END";

        Connection databaseConnection = DatabaseConfiguration.getConnection();
        Statement stmt = databaseConnection.createStatement();

        stmt.execute(createTableProductsSql);
        stmt.execute(deleteProcedureProducts);
        stmt.execute(createStoredProcedureProducts);

        stmt.execute(createTableLockersSql);
        stmt.execute(deleteProcedureLockers);
        stmt.execute(createStoredProcedureLockers);
    }
}
