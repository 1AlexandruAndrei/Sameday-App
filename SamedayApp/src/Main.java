import Delivery.Driver;
import Delivery.Warehouse;
import com.sun.jdi.request.WatchpointRequest;
import exception.InvalidDataException;
import orderInfo.Order;
import orderInfo.PremiumUser;
import orderInfo.Product;
import service.*;

import javax.xml.crypto.Data;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<PremiumUser> premiumUsers = new ArrayList<>();
        Warehouse warehouse1 = null;
        Warehouse warehouse2 = null;
        warehouse1 = new Warehouse(1, "Militari Petrom", 10000);
        warehouse2 = new Warehouse(2, "Nordului Nowa", 5);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Are you a user or a driver?");
        System.out.println("1. User");
        System.out.println("2. Driver");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {

            try {
                UserService.createUser("Andrei", "admin", "andrei@test.com", "+40777777777", true);
                //UserService.createUser("Jean", "parola", "jean@test.com", "+40777777770", false);
            } catch (InvalidDataException e) {
                System.out.println("Invalid user data: " + e.getMessage());
            }

            DataStorage.createProducts();
            DataStorage.addLocker();

            Driver driver = new Driver(1, "Sorin");

            List<Product> products = ProductService.getProductList();

            //The list of products can be sorted alphabetically
            //Collections.sort(products, (product1, product2) -> product1.getName().compareTo(product2.getName()));
            //System.out.println("Sorted list of products:");

            for (Product product : products) {
                System.out.println(product.getName() + ": $" + product.getPrice());
            }
            List<Product> order1Products = Arrays.asList(products.get(0), products.get(1));
            List<Product> order2Products = Arrays.asList(products.get(2), products.get(3));
            OrderService.createOrder(UserService.getUserList().get(0), order1Products, "Str Preciziei nr 24", "10:00 AM", driver);
            OrderService.createOrder(UserService.getUserList().get(0), order2Products, "Soseaua Nordului 52 ", "1:00 PM", driver);

            FeedbackService.provideFeedbackForOrders(scanner);

            for (Order order : OrderService.getOrderList()) {
                double totalOrderValue = OrderService.calculateTotalOrderValue(order);
                System.out.println("-----------------------------------------------");
                System.out.println("Total order value for Order " + order.getOrderId() + ": $" + totalOrderValue);
                System.out.println("-----------------------------------------------");
            }

            DataStorage.displayAll();
            DriverService.displayDriver(driver);


            List<Order> orders = OrderService.getOrderList();
            for (int i = 0; i < orders.size(); i++) {
                Warehouse warehouse = (i % 2 == 0) ? warehouse1 : warehouse2; // Alternating warehouses so they don't get full
                orders.get(i).setWarehouse(warehouse);
            }

            WarehouseService.displayWarehouse(warehouse1);
            WarehouseService.displayWarehouse(warehouse2);
        }

        else if (choice == 2)
        {

            Driver driver = new Driver(1, "Sorin");

            Order order1 = new Order(1);
            Order order2 = new Order(2);

            DataStorage.createProducts2();

            List<Order> orders = Arrays.asList(order1, order2);

            for (Order order : orders) {
                driver.assignOrder(order);
            }
            DataStorage.createWarehouses();


            if (warehouse1 != null && warehouse2 != null) {
                order1.setWarehouse(warehouse1);
                order2.setWarehouse(warehouse2);
            } else {
                System.out.println("Warehouses not initialized.");
            }

        } else {
            System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}