import Delivery.Driver;
import Delivery.Warehouse;
import orderInfo.*;

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
            // User related code

            // There are 2 types of users, premium and normal. The difference is that premium users get a 10% discount of the total order value.
            // Service.addPremiumUser("Marius", "pass", "marius@user.com", "+40000000000");
            Service.createUser("Andrei", "admin", "andrei@test.com", "+40777777777", true);

            Service.createProduct("T-shirt", 250);
            Service.createProduct("iPhone 15 Pro Max", 1000);
            Service.createProduct("Lamborghini Aventador", 100000);
            Service.createProduct("Sprite", 7.5);
            Service.createProduct("Jeans", 203);
            Service.createProduct("PC", 10000);

            Service.addLocker(10, false); // Non-available locker
            Service.addLocker(5, true);   // Available locker
            Service.addLocker(9, true);   // Available locker

            Driver driver = new Driver(1, "Sorin");

            Warehouse warehouse1 = new Warehouse(1, "Militari Petrom", 10000);
            Warehouse warehouse2 = new Warehouse(2, "Nordului Nowa", 5);

            User user = Service.getUserList().get(0); // first user
            List<Product> products = Service.getProductList();
            List<Product> order1Products = Arrays.asList(products.get(0), products.get(1)); // First two products
            List<Product> order2Products = Arrays.asList(products.get(2), products.get(3)); // Second two products
            Service.createOrder(user, order1Products, "Str Preciziei nr 24", "10:00 AM", driver);
            Service.createOrder(user, order2Products, "Soseaua Nordului 52 ", "1:00 PM", driver);

            Service.provideFeedbackForOrders(scanner);

            for (Order order : Service.getOrderList()) {
                double totalOrderValue = Service.calculateTotalOrderValue(order);
                System.out.println("-----------------------------------------------");
                System.out.println("Total order value for Order " + order.getOrderId() + ": $" + totalOrderValue);
                System.out.println("-----------------------------------------------");
            }

            Service.displayUser(user);
            Service.displayDriver(driver);
            Service.displayFeedbackForOrder(1);
            Service.displayFeedbackForOrder(2);

            List<Order> orders = Service.getOrderList();
            for (int i = 0; i < orders.size(); i++) {
                Warehouse warehouse = (i % 2 == 0) ? warehouse1 : warehouse2; // Alternating warehouses so they don't get full
                orders.get(i).setWarehouse(warehouse);
            }

            Service.displayWarehouse(warehouse1);
            Service.displayWarehouse(warehouse2);
        } else if (choice == 2) {

            Driver driver = new Driver(1, "Sorin");

            Order order1 = new Order(1);
            Order order2 = new Order(2);

            Service.createProduct("T-shirt", 250);
            Service.createProduct("iPhone 15 Pro Max", 1000);
            Service.createProduct("Lamborghini Aventador", 100000);
            Service.createProduct("Sprite", 7.5);

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
