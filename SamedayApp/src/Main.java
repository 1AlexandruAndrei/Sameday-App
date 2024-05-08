
import Delivery.*;
import config.DataSetup;
import config.DatabaseConfiguration;
import exception.InvalidDataException;
import orderInfo.Order;
import orderInfo.Product;
import orderInfo.User;
import service.*;

import java.sql.SQLException;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        Warehouse warehouse1 = null;
        Warehouse warehouse2 = null;
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. User");
        System.out.println("2. Driver");
        System.out.println("3. ADMIN LOGIN ONLY");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            try {
                DatabaseConfiguration.getConnection();
                DataSetup dataSetup = new DataSetup();
                dataSetup.createTableAndStoredProcedure();
                UserService.createUser("Andrei", "admin", "andrei@test.com", "+40777777777", true);
            } catch (InvalidDataException e) {
                System.out.println("Invalid user data: " + e.getMessage());
                return;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ProductService.displayProductsFromDatabase();
            List<Product> selectedProducts = new ArrayList<>();
            System.out.println("Enter product ID (0 to stop): ");
            int productIdToOrder;
            while ((productIdToOrder = scanner.nextInt()) != 0)
            {
                Product selectedProduct = ProductService.getProductById(productIdToOrder);
                if (selectedProduct != null) {
                    selectedProducts.add(selectedProduct);
                } else {
                    System.out.println("No product found");
                }
            }

            User user = UserService.getUserList().get(0);

            if (!selectedProducts.isEmpty()) {
                Driver driver = new Driver(1, "Sorin");
                String deliveryAddress = "Delivery Address";
                String deliveryTime = "Delivery Time";
                OrderService.createOrder(user, selectedProducts, deliveryAddress, deliveryTime, driver);
                System.out.println("Order placed successfully!");
            } else {
                System.out.println("No products selected for order.");
            }

        }


        if (choice == 2)
        {
            Driver driver = new Driver(1, "Sorin");
            Order order2 = new Order(2);
            DataStorage.createWarehouses();

            driver.assignOrder(order2);
            System.out.println("Order that must be delivered: " + order2.getOrderId());
        }

        if (choice == 3) {
            try {
                DatabaseConfiguration.getConnection();
                DataSetup dataSetup = new DataSetup();
                dataSetup.createTableAndStoredProcedure();

                System.out.println("1. Create a product");
                System.out.println("2. Read a product");
                System.out.println("3. Update a product");
                System.out.println("4. Delete a product");
                System.out.print("Enter your choice (1-4): ");
                int operationChoice = scanner.nextInt();
                scanner.nextLine();

                switch (operationChoice) {
                    case 1:
                        System.out.print("Enter product name: ");
                        String productName = scanner.nextLine();
                        System.out.print("Enter product price: ");
                        double productPrice = scanner.nextDouble();
                        DataStorage.addProduct(productName, productPrice);
                        break;
                    case 2:
                        System.out.print("Enter the ID of the product: ");
                        int productIdToRead = scanner.nextInt();
                        scanner.nextLine();
                        Product product = ProductService.getProductById(productIdToRead);
                        if (product != null) {
                            System.out.println("Product ID: " + product.getProductId());
                            System.out.println("Product Name: " + product.getName());
                            System.out.println("Product Price: " + product.getPrice());
                        } else {
                            System.out.println("No product found with ID " + productIdToRead);
                        }
                        break;
                    case 3:
                        System.out.print("Enter the ID of the product: ");
                        int productIdToUpdate = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("New name:");
                        String newName = scanner.nextLine();
                        System.out.print("New price:");
                        String newPriceInput = scanner.nextLine();
                        double newPrice = -1;
                        if (!newPriceInput.isEmpty()) {
                            newPrice = Double.parseDouble(newPriceInput);
                        }
                        ProductService.updateProduct(productIdToUpdate, newName, newPrice);
                        break;
                    case 4:
                        System.out.print("Enter the ID of the product: ");
                        int productIdToDelete = scanner.nextInt();
                        scanner.nextLine();
                        ProductService.deleteProductById(productIdToDelete);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 4.");
                        break;
                }
                System.out.println("Available Products: ");
                ProductService.displayProductsFromDatabase();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }

}