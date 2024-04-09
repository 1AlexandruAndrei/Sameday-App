package Delivery;

import orderInfo.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Warehouse {
    private int warehouseId;
    private String location;
    private int capacity;
    private List<Order> orders;



    public Warehouse(int warehouseId, String location, int capacity) {
        this.warehouseId = warehouseId;
        this.location = location;
        this.capacity = capacity;
        this.orders = new ArrayList<>();
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Order> getOrders() {
        return orders;
    }



}
