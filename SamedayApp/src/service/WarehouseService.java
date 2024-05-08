package service;

import Delivery.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class WarehouseService {
    private static WarehouseService instance;
    private final List<Warehouse> warehouseList = new ArrayList<>();
    private int nextWarehouseId = 1;

    private WarehouseService() {}

    public static WarehouseService getInstance() {
        if (instance == null) {
            instance = new WarehouseService();
        }
        return instance;
    }

    public void addWarehouse(String location, int capacity) {
        Warehouse warehouse = new Warehouse(nextWarehouseId++, location, capacity);
        warehouseList.add(warehouse);
    }

    public void displayWarehouse(Warehouse warehouse) {
        System.out.println("--------------------------");
        System.out.println("Warehouse ID: " + warehouse.getWarehouseId());
        System.out.println("Location: " + warehouse.getLocation());
        System.out.println("Capacity: " + warehouse.getCapacity());
        System.out.println("--------------------------");
        System.out.println();
    }

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }
}
