import Delivery.Warehouse;
import config.DatabaseConfiguration;
import Delivery.Locker;
import orderInfo.Order;
import orderInfo.Product;
import orderInfo.User;
import service.*;
import tracker.CRUDtracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataStorage {
    public static void displayLockers() {
        List<Locker> lockerList = LockerService.getLockerList();
        System.out.println("List of Lockers:");
        for (Locker locker : lockerList) {
            System.out.println("Locker ID: " + locker.getLockerId());
            System.out.println("Location: " + locker.getLocation());
            System.out.println("Size: " + locker.getSize());
            System.out.println("Availability: " + locker.isAvailable());
            System.out.println();
        }
    }

    public static void displayWarehouses() {
        List<Warehouse> warehouseList = WarehouseService.getWarehouseList();
        System.out.println("List of Warehouses:");
        for (Warehouse warehouse : warehouseList) {
            System.out.println("Warehouse ID: " + warehouse.getWarehouseId());
            System.out.println("Location: " + warehouse.getLocation());
            System.out.println("Capacity: " + warehouse.getCapacity());
            System.out.println();
        }
    }
    public static void displayUsers() {
        List<User> userList = UserService.getUserList();
        System.out.println("Users:");
        for (User user : userList) {
            System.out.println("User ID: " + user.getUserId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone Number: " + user.getPhoneNumber());
            System.out.println("-----------------------");
        }
    }

    public static void displayProducts() {
        List<Product> productList = ProductService.getProductList();
        System.out.println("Products:");
        for (Product product : productList) {
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: " + product.getPrice());
            System.out.println("-----------------------");
        }
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

    //////////////////////////////////////

    public static void CRUD_locker(Scanner scanner) {
        System.out.println("1. Create a locker");
        System.out.println("2. Read a locker");
        System.out.println("3. Update a locker");
        System.out.println("4. Delete a locker");
        System.out.print("Enter your choice (1-4): ");
        int operationChoice = scanner.nextInt();
        scanner.nextLine();

        switch (operationChoice) {
            case 1:
                System.out.print("Enter locker location: ");
                String lockerLocation = scanner.nextLine();
                System.out.print("Enter locker size: ");
                int lockerSize = scanner.nextInt();
                System.out.print("Enter locker availability (true/false): ");
                boolean lockerAvailability = scanner.nextBoolean();
                DataStorage.addLocker(lockerLocation, lockerSize, lockerAvailability);
                CRUDtracker.recordOperation("Create Locker");
                break;
            case 2:
                System.out.print("Enter the ID of the locker: ");
                int lockerIdToRead = scanner.nextInt();
                scanner.nextLine();
                Locker locker = LockerService.getLockerById(lockerIdToRead);
                if (locker != null) {
                    System.out.println("Locker ID: " + locker.getLockerId());
                    System.out.println("Location: " + locker.getLocation());
                    System.out.println("Size: " + locker.getSize());
                    System.out.println("Availability: " + locker.isAvailable());
                } else {
                    System.out.println("No locker found with ID " + lockerIdToRead);
                }
                CRUDtracker.recordOperation("Read Locker");
                break;
            case 3:
                System.out.print("Enter the ID of the locker: ");
                int lockerIdToUpdate = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New location:");
                String newLocation = scanner.nextLine();
                System.out.print("New size:");
                int newSize = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New availability (true/false):");
                boolean newAvailability = scanner.nextBoolean();
                LockerService.updateLocker(lockerIdToUpdate, newLocation, newSize, newAvailability);
                CRUDtracker.recordOperation("Update Locker");
                break;
            case 4:
                System.out.print("Enter the ID of the locker: ");
                int lockerIdToDelete = scanner.nextInt();
                scanner.nextLine();
                LockerService.deleteLockerById(lockerIdToDelete);
                CRUDtracker.recordOperation("Delete Locker");
                break;
            default:
                System.out.println("Invalid choice. Please enter a number from 1 to 4.");
                break;
        }
        DataStorage.displayLockers();
    }

    public static void CRUD_product(Scanner scanner) throws SQLException {
        System.out.println("5. Create a product");
        System.out.println("6. Read a product");
        System.out.println("7. Update a product");
        System.out.println("8. Delete a product");
        System.out.print("Enter your choice (5-8): ");
        int operationChoice = scanner.nextInt();
        scanner.nextLine();

        switch (operationChoice) {
            case 5:
                System.out.print("Enter product name: ");
                String productName = scanner.nextLine();
                System.out.print("Enter product price: ");
                double productPrice = scanner.nextDouble();
                DataStorage.addProduct(productName, productPrice);
                CRUDtracker.recordOperation("Create Product");
                break;
            case 6:
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
                CRUDtracker.recordOperation("Read Product");
                break;
            case 7:
                System.out.print("Enter the ID of the product: ");
                int productIdToUpdate = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New name:");
                String newName = scanner.nextLine();
                System.out.print("New price:");
                double newPrice = scanner.nextDouble();
                scanner.nextLine();
                ProductService.updateProduct(productIdToUpdate, newName, newPrice);
                CRUDtracker.recordOperation("Update Product");
                break;
            case 8:
                System.out.print("Enter the ID of the product: ");
                int productIdToDelete = scanner.nextInt();
                scanner.nextLine();
                ProductService.deleteProductById(productIdToDelete);
                CRUDtracker.recordOperation("Delete Product");
                break;
            default:
                System.out.println("Invalid choice. Please enter a number from 5 to 8.");
                break;
        }
        DataStorage.displayProducts();
    }

    public static void CRUD_user(Scanner scanner)
    {
        System.out.println("9. Create a user");
        System.out.println("10. Read a user");
        System.out.println("11. Update a user");
        System.out.println("12. Delete a user");
        System.out.print("Enter your choice (9-12): ");
        int operationChoice = scanner.nextInt();
        scanner.nextLine();

        switch (operationChoice) {
            case 9:
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                System.out.print("Enter email: ");
                String email = scanner.nextLine();
                System.out.print("Enter phone number: ");
                String phoneNumber = scanner.nextLine();
                UserService.getInstance().createUser(username, password, email, phoneNumber);
                CRUDtracker.recordOperation("Create User");
                break;
            case 10:
                System.out.print("Enter the ID of the user: ");
                int userIdToRead = scanner.nextInt();
                scanner.nextLine();
                User user = UserService.getUserById(userIdToRead);
                if (user != null) {
                    System.out.println("User ID: " + user.getUserId());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Phone Number: " + user.getPhoneNumber());
                } else {
                    System.out.println("No user found with ID " + userIdToRead);
                }
                CRUDtracker.recordOperation("Read User");
                break;
            case 11:
                System.out.print("Enter the ID of the user: ");
                int userIdToUpdate = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter new username: ");
                String newUsername = scanner.nextLine();
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine();
                System.out.print("Enter new phone number: ");
                String newPhoneNumber = scanner.nextLine();
                UserService.getInstance().updateUser(userIdToUpdate, newUsername, newPassword, newEmail, newPhoneNumber);
                CRUDtracker.recordOperation("Update User");
                break;
            case 12:
                System.out.print("Enter the ID of the user: ");
                int userIdToDelete = scanner.nextInt();
                scanner.nextLine();
                UserService.getInstance().deleteUserById(userIdToDelete);
                CRUDtracker.recordOperation("Delete User");
                break;
            default:
                System.out.println("Invalid choice. Please enter a number from 9 to 12.");
                break;
        }
        DataStorage.displayUsers();
    }

    public static void CRUD_warehouse(Scanner scanner) {
        System.out.println("13. Create a warehouse");
        System.out.println("14. Read a warehouse");
        System.out.println("15. Update a warehouse");
        System.out.println("16. Delete a warehouse");
        System.out.print("Enter your choice (13-16): ");
        int operationChoice = scanner.nextInt();
        scanner.nextLine();

        switch (operationChoice) {
            case 13:
                System.out.print("Enter warehouse location: ");
                String warehouseLocation = scanner.nextLine();
                System.out.print("Enter warehouse capacity: ");
                int warehouseCapacity = scanner.nextInt();
                WarehouseService.getInstance().createWarehouse(warehouseLocation, warehouseCapacity);
                CRUDtracker.recordOperation("Create Warehouse");
                break;
            case 14:
                System.out.print("Enter the ID of the warehouse: ");
                int warehouseIdToRead = scanner.nextInt();
                scanner.nextLine();
                Warehouse warehouse = WarehouseService.getWarehouseById(warehouseIdToRead);
                if (warehouse != null) {
                    System.out.println("Warehouse ID: " + warehouse.getWarehouseId());
                    System.out.println("Location: " + warehouse.getLocation());
                    System.out.println("Capacity: " + warehouse.getCapacity());
                } else {
                    System.out.println("No warehouse found with ID " + warehouseIdToRead);
                }
                CRUDtracker.recordOperation("Read Warehouse");
                break;
            case 15:
                System.out.print("Enter the ID of the warehouse: ");
                int warehouseIdToUpdate = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New location:");
                String newLocation = scanner.nextLine();
                System.out.print("New capacity:");
                int newCapacity = scanner.nextInt();
                scanner.nextLine();
                System.out.print("New availability (true/false):");
                boolean newAvailability = scanner.nextBoolean();
                WarehouseService.updateWarehouse(warehouseIdToUpdate, newLocation, newCapacity);
                CRUDtracker.recordOperation("Update Warehouse");
                break;
            case 16:
                System.out.print("Enter the ID of the warehouse: ");
                int warehouseIdToDelete = scanner.nextInt();
                scanner.nextLine();
                WarehouseService.deleteWarehouseById(warehouseIdToDelete);
                CRUDtracker.recordOperation("Delete Warehouse");
                break;
            default:
                System.out.println("Invalid choice. Please enter a number from 13 to 16.");
                break;
        }
        DataStorage.displayWarehouses();
    }

    public static void CRUD_order(Scanner scanner) {
        System.out.println("1. Create an order");
        System.out.println("2. Read an order");
        System.out.println("3. Update an order");
        System.out.println("4. Delete an order");
        System.out.print("Enter your choice (1-4): ");
        int operationChoice = scanner.nextInt();
        scanner.nextLine();

        switch (operationChoice) {
            case 1:
                createOrder(scanner);
                break;
            case 2:
                readOrder(scanner);
                break;
            case 3:
                updateOrder(scanner);
                break;
            case 4:
                deleteOrder(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please enter a number from 1 to 4.");
                break;
        }
    }

    public static void createOrder(Scanner scanner) {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        User user = UserService.getInstance().getUserById(userId);

        System.out.print("Enter delivery address: ");
        String deliveryAddress = scanner.nextLine();

        System.out.print("Enter delivery time: ");
        String deliveryTime = scanner.nextLine();

        List<Product> selectedProducts = new ArrayList<>();
        System.out.print("Enter number of products: ");
        int numOfProducts = scanner.nextInt();
        scanner.nextLine();

        displayProducts();
        for (int i = 0; i < numOfProducts; i++) {
            System.out.print("Enter product ID: ");
            int productId = scanner.nextInt();
            scanner.nextLine();
            Product product = ProductService.getInstance().getProductById(productId);
            if (product != null) {
                selectedProducts.add(product);
            } else {
                System.out.println("Product with ID " + productId + " not found.");
            }
        }

        System.out.print("Enter feedback ID (1-5): ");
        int feedbackId = scanner.nextInt();
        scanner.nextLine();

        OrderService.getInstance().createOrderInDatabase(user, selectedProducts, deliveryAddress, deliveryTime, feedbackId);
    }
    private static void readOrder(Scanner scanner) {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order order = OrderService.getOrderByIdFromDatabase(orderId);
        if (order != null) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("User ID: " + order.getUser().getUserId());
            System.out.println("Delivery Address: " + order.getDeliveryAddress());
            System.out.println("Delivery Time: " + order.getDeliveryTime());
            System.out.println("Total Order Value: $" + order.getTotalOrderValue());
            System.out.println("Products in the Order: ");
            for (Product product : order.getProducts()) {
                System.out.println("- Product ID: " + product.getProductId() + ", Name: " + product.getName() + ", Price: $" + product.getPrice());
            }
        } else {
            System.out.println("No order found with ID " + orderId);
        }
    }

    private static void updateOrder(Scanner scanner) {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        Order existingOrder = OrderService.getOrderByIdFromDatabase(orderId);
        if (existingOrder == null) {
            System.out.println("No order found with ID " + orderId);
            return;
        }

        System.out.print("Enter new delivery address: ");
        String newDeliveryAddress = scanner.nextLine();

        System.out.print("Enter new delivery time: ");
        String newDeliveryTime = scanner.nextLine();

        List<Product> newProducts = new ArrayList<>();
        System.out.print("Enter number of new products: ");
        int numOfNewProducts = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numOfNewProducts; i++) {
            System.out.print("Enter new product ID: ");
            int newProductId = scanner.nextInt();
            scanner.nextLine();
            Product newProduct = ProductService.getInstance().getProductById(newProductId); // Assuming ProductService exists
            if (newProduct != null) {
                newProducts.add(newProduct);
            } else {
                System.out.println("Product with ID " + newProductId + " not found.");
            }
        }

        existingOrder.setDeliveryAddress(newDeliveryAddress);
        existingOrder.setDeliveryTime(newDeliveryTime);
        existingOrder.setProducts(newProducts);
        existingOrder.setTotalOrderValue(newProducts.stream().mapToDouble(Product::getPrice).sum());

        OrderService.updateOrder(existingOrder);
    }

    private static void deleteOrder(Scanner scanner) {
        System.out.print("Enter order ID to delete: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        OrderService.deleteOrderById(orderId);
    }



}
