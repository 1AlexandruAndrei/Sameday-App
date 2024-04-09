import Delivery.Driver;
import Delivery.Warehouse;
import com.sun.jdi.request.WatchpointRequest;
import exception.InvalidDataException;
import orderInfo.Order;
import orderInfo.Product;
import service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Are you a user or a driver?");
        System.out.println("1. User");
        System.out.println("2. Driver");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {

            try {
                UserService.createUser("Andrei", "admin", "andrei@test.com", "+40777777777", true);
            } catch (InvalidDataException e) {
                System.out.println("Invalid user data: " + e.getMessage());
            }

            ProductService.createProduct("T-shirt", 250);
            ProductService.createProduct("iPhone 15 Pro Max", 1000);
            ProductService.createProduct("Lamborghini Aventador", 100000);
            ProductService.createProduct("Sprite", 7.5);
            ProductService.createProduct("Jeans", 203);
            ProductService.createProduct("PC", 10000);

            LockerService.addLocker(10, false); // Non-available locker
            LockerService.addLocker(5, true);   // Available locker
            LockerService.addLocker(9, true);   // Available locker

            Driver driver = new Driver(1, "Sorin");

            Warehouse warehouse1 = new Warehouse(1, "Militari Petrom", 10000);
            Warehouse warehouse2 = new Warehouse(2, "Nordului Nowa", 5);

            List<Product> products = ProductService.getProductList();
            List<Product> order1Products = Arrays.asList(products.get(0), products.get(1)); // First two products
            List<Product> order2Products = Arrays.asList(products.get(2), products.get(3)); // Second two products
            OrderService.createOrder(UserService.getUserList().get(0), order1Products, "Str Preciziei nr 24", "10:00 AM", driver);
            OrderService.createOrder(UserService.getUserList().get(0), order2Products, "Soseaua Nordului 52 ", "1:00 PM", driver);

            FeedbackService.provideFeedbackForOrders(scanner);

            for (Order order : OrderService.getOrderList()) {
                double totalOrderValue = OrderService.calculateTotalOrderValue(order);
                System.out.println("-----------------------------------------------");
                System.out.println("Total order value for Order " + order.getOrderId() + ": $" + totalOrderValue);
                System.out.println("-----------------------------------------------");
            }

            UserService.displayUser(UserService.getUserList().get(0));
            DriverService.displayDriver(driver);
            FeedbackService.displayFeedbackForOrder(1);
            FeedbackService.displayFeedbackForOrder(2);

            List<Order> orders = OrderService.getOrderList();
            for (int i = 0; i < orders.size(); i++) {
                Warehouse warehouse = (i % 2 == 0) ? warehouse1 : warehouse2; // Alternating warehouses so they don't get full
                orders.get(i).setWarehouse(warehouse);
            }

            WarehouseService.displayWarehouse(warehouse1);
            WarehouseService.displayWarehouse(warehouse2);
        } else if (choice == 2) {

            Driver driver = new Driver(1, "Sorin");

            Order order1 = new Order(1);
            Order order2 = new Order(2);

            ProductService.createProduct("T-shirt", 250);
            ProductService.createProduct("iPhone 15 Pro Max", 1000);
            ProductService.createProduct("Lamborghini Aventador", 100000);
            ProductService.createProduct("Sprite", 7.5);

            List<Order> orders = Arrays.asList(order1, order2);

            for (Order order : orders) {
                driver.assignOrder(order);
            }

            Warehouse warehouse1 = new Warehouse(1, "Militari Petrom", 10000);
            Warehouse warehouse2 = new Warehouse(2, "Nordului Nowa", 5);

            order1.setWarehouse(warehouse1);
            order2.setWarehouse(warehouse2);

            System.out.println("Driver: " + driver.getName());
            System.out.println("Active Orders:");
            for (Order order : driver.getActiveOrders()) {
                System.out.println("- Order ID: " + order.getOrderId() + ", Warehouse: " + order.getWarehouse().getLocation());
            }

            System.out.println("\nWarehouses:");
            System.out.println("- Warehouse 1: " + warehouse1.getLocation());
            System.out.println("- Warehouse 2: " + warehouse2.getLocation());
        } else {
            System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}
