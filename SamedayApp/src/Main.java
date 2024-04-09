import Delivery.Driver;
import Delivery.Warehouse;
import orderInfo.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Service.addPremiumUser("John", "john123", "john@example.com", "123456789");
        Service.createUser("Andrei", "admin", "andrei@test.com", "+40777777777", false);

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

        Warehouse warehouse1 = new Warehouse(1, "Militari Petrom", 10);
        Warehouse warehouse2 = new Warehouse(2, "Nordului Nowa", 5);

        /////////////////////////////////////////////////////////////////////////////


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
            Warehouse warehouse = (i % 2 == 0) ? warehouse1 : warehouse2; // Alternating warehouses so they dont get full
            orders.get(i).setWarehouse(warehouse);
        }

        Service.displayWarehouse(warehouse1);
        Service.displayWarehouse(warehouse2);

        scanner.close();
    }
}
