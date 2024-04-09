package Delivery;

import orderInfo.Order;

import java.util.Set;
import java.util.TreeSet;

public class Driver {
    private int driverId;
    private String name;
    protected Set<Order> activeOrders;

    public Driver(int driverId, String name) {
        this.driverId = driverId;
        this.name = name;
        this.activeOrders = new TreeSet<>(); // Using TreeSet for sorting
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Order> getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(Set<Order> activeOrders) {
        this.activeOrders = activeOrders;
    }

    public void assignOrder(Order order) {
        activeOrders.add(order);
    }
}
