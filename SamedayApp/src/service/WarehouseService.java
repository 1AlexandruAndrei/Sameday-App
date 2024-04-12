package service;

import Delivery.*;
import java.util.*;

public class WarehouseService {
    private static final List<Warehouse> warehouseList = new ArrayList<>();
    private static int nextWarehouseId = 1;

    public static void addWarehouse(String location, int capacity) {
        Warehouse warehouse = new Warehouse(nextWarehouseId++, location, capacity);
        warehouseList.add(warehouse);
    }

    public static void displayWarehouse(Warehouse warehouse) {
        System.out.println("--------------------------");
        System.out.println("Warehouse ID: " + warehouse.getWarehouseId());
        System.out.println("Location: " + warehouse.getLocation());
        System.out.println("Capacity: " + warehouse.getCapacity());
        System.out.println("--------------------------");
        System.out.println();
    }

    public static List<Warehouse> getWarehouseList() {
        return warehouseList;
    }
}
