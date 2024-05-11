package service;

import Delivery.Driver;
import orderInfo.Order;

import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private static DriverService instance;
    private final List<Driver> driverList = new ArrayList<>();

    private DriverService() {}

    public static DriverService getInstance() {
        if (instance == null) {
            instance = new DriverService();
        }
        return instance;
    }

    public void assignDriverToOrder(Order order, Driver driver) {
        driver.assignOrder(order);
        System.out.println("Order assigned to " + driver.getName());
    }

    public List<Order> getAssignedOrders(Driver driver) {
        List<Order> assignedOrders = new ArrayList<>();
        for (Order order : driver.getActiveOrders()) {
            assignedOrders.add(order);
        }
        return assignedOrders;
    }
}
