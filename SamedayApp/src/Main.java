
import Delivery.*;
import config.DataSetup;
import config.DatabaseConfiguration;
import exception.InvalidDataException;
import orderInfo.Order;
import orderInfo.Product;
import orderInfo.User;
import service.*;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Create an order");
        System.out.println("2. Driver Info");
        System.out.println("3. ADMIN CRUD OPERATIONS ONLY");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            try {
                DatabaseConfiguration.getConnection();
                DataSetup dataSetup = new DataSetup();
                dataSetup.createTableAndStoredProcedure();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            System.out.print("Enter user ID: ");
            int userId = scanner.nextInt();
            scanner.nextLine();
            User user = UserService.getUserById(userId);

            if (user != null) {
                ProductService.displayProductsFromDatabase();
                List<Product> selectedProducts = new ArrayList<>();
                System.out.println("Enter product ID (0 to stop): ");
                int productIdToOrder;
                while ((productIdToOrder = scanner.nextInt()) != 0) {
                    Product selectedProduct = ProductService.getProductById(productIdToOrder);
                    if (selectedProduct != null) {
                        selectedProducts.add(selectedProduct);
                    } else {
                        System.out.println("No product found.");
                    }
                }

                if (!selectedProducts.isEmpty()) {
                    Driver driver = new Driver(1, "Sorin");
                    String deliveryAddress = "Texas";
                    String deliveryTime = "20 May 2024";
                    OrderService.createOrder(user, selectedProducts, deliveryAddress, deliveryTime, driver);
                    System.out.println("Order placed successfully!");
                } else {
                    System.out.println("Please try again and add products.");
                }
            } else {
                System.out.println("User not found.");
            }
        }


        if (choice == 2) {
            Driver driver = new Driver(1, "Sorin");
            Order order2 = new Order(2);
            //DataStorage.createWarehouses();

            driver.assignOrder(order2);
            System.out.println("Order that must be delivered: " + order2.getOrderId());
        }

        if (choice == 3) {
            try {
                DatabaseConfiguration.getConnection();
                DataSetup dataSetup = new DataSetup();
                dataSetup.createTableAndStoredProcedure();

                System.out.println("1. Locker CRUD");
                System.out.println("2. Product CRUD");
                System.out.println("3. User CRUD");
                System.out.println("4. Warehouse CRUD");
                System.out.print("Enter your choice (1-4): ");
                int categoryChoice = scanner.nextInt();
                scanner.nextLine();

                switch (categoryChoice) {
                    case 1:
                        DataStorage.CRUD_locker(scanner);
                        break;
                    case 2:
                        DataStorage.CRUD_product(scanner);
                        break;
                    case 3:
                        DataStorage.CRUD_user(scanner);
                        break;
                    case 4:
                        DataStorage.CRUD_warehouse(scanner);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 4.");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}