import Delivery.*;
import config.DataSetup;
import config.DatabaseConfiguration;
import orderInfo.Order;
import service.*;
import java.sql.SQLException;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. Create an order");
        System.out.println("2. Driver Info");
        System.out.println("3. ADMIN CRUD OPERATIONS");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1)
        {
            DataStorage.CRUD_order(scanner);
        }

        if (choice == 2) {
            Driver driver = new Driver(1, "Sorin");
            System.out.println("Hello, " + driver.getName());

            Warehouse packageWarehouse = WarehouseService.getWarehouseById(3);
            System.out.println("Drop the package at the warehouse situated at " + packageWarehouse.getLocation());

            System.out.print("\nEnter the order ID you want to deliver: ");
            int orderId = scanner.nextInt();
            scanner.nextLine();

            Order order = OrderService.getInstance().getOrderByIdFromDatabase(orderId);

            if (order != null) {
                driver.assignOrder(order);
                System.out.println("An order was assigned to you!");
                System.out.println("Order " + order.getOrderId() + " must be delivered to " + order.getDeliveryAddress());
                System.out.println("Order must be delivered by the date: " + order.getDeliveryTime());
            } else {
                System.out.println("Order not found");
            }
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
                        System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}