package service;

import Delivery.Driver;
import orderInfo.Order;

import java.util.ArrayList;
import java.util.List;

public class DriverService {
    private static final List<Driver> driverList = new ArrayList<>();

    public static void assignDriverToOrder(Order order, Driver driver) {
        driver.assignOrder(order);
        System.out.println("Order assigned to " + driver.getName());
    }

    public static void displayDriver(Driver driver) {
        System.out.println("The delivery driver assigned to your order is " + driver.getName());
        System.out.println("ID of the orders that will be delivered by " + driver.getName() + " are: ");
        for (Order order : driver.getActiveOrders()) {
            System.out.println(order.getOrderId());
        }
        System.out.println();
    }

    public static List<Order> getAssignedOrders(Driver driver) {
        List<Order> assignedOrders = new ArrayList<>();
        for (Order order : driver.getActiveOrders()) {
            assignedOrders.add(order);
        }
        return assignedOrders;
    }
}
