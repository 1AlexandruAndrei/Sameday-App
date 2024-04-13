import Delivery.Warehouse;
import service.*;

import static service.WarehouseService.displayWarehouse;

public class DataStorage {
    public static void createProducts()
    {
        ProductService.createProduct("T-shirt", 50);
        ProductService.createProduct("iPhone 15 Pro Max", 1000);
        ProductService.createProduct("Lamborghini Aventador", 100000);
        ProductService.createProduct("Sprite", 7.5);
        ProductService.createProduct("Jeans", 200);
        ProductService.createProduct("PC", 10000);
        ProductService.createProduct("Dom Perignon", 1250.5);
        ProductService.createProduct("Cartea 'Felicitari'", 100);

    }
    public static void addLocker()
    {
        LockerService.addLocker(10, false); // Non-available locker
        LockerService.addLocker(5, true);   // Available locker
        LockerService.addLocker(9, true);   // Available locker
    }

    public static void createProducts2()
    {
        ProductService.createProduct("T-shirt", 250);
        ProductService.createProduct("iPhone 15 Pro Max", 1000);
        ProductService.createProduct("Lamborghini Aventador", 100000);
        ProductService.createProduct("Sprite", 7.5);
    }

    public static void createWarehouses() {
        Warehouse warehouse1 = new Warehouse(1, "Militari Petrom", 10000);
        WarehouseService.addWarehouse(warehouse1.getLocation(), warehouse1.getCapacity());
        displayWarehouse(warehouse1);

        Warehouse warehouse2 = new Warehouse(2, "Nordului Nowa", 5);
        WarehouseService.addWarehouse(warehouse2.getLocation(), warehouse2.getCapacity());
        displayWarehouse(warehouse2);
    }

    public static void displayAll()
    {
        UserService.displayUser(UserService.getUserList().get(0));
        FeedbackService.displayFeedbackForOrder(1);
        FeedbackService.displayFeedbackForOrder(2);
    }



}
