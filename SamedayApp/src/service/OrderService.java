package service;

import Delivery.Driver;
import orderInfo.Order;
import orderInfo.PremiumUser;
import orderInfo.Product;
import orderInfo.User;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class OrderService {
    private static OrderService instance;
    private static final List<Order> orderList = new ArrayList<>();
    private static int nextOrderId = 1;
    private static final Scanner scanner = new Scanner(System.in);

    private OrderService() {}

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public static void createOrder(User user, List<Product> selectedProducts, String deliveryAddress, String deliveryTime, Driver driver) {

        if (selectedProducts.isEmpty()) {
            System.out.println("NO PRODUCTS SELECTED. THE ORDER WAS NOT CREATED.");
            return;
        }

        User retrievedUser = UserService.getInstance().getUserById(user.getUserId());
        if (retrievedUser != null) {
            Order newOrder = new Order(nextOrderId++, retrievedUser, selectedProducts, deliveryAddress, deliveryTime);
            orderList.add(newOrder);
            updateOrderTotal(newOrder);

            // - 10 USD discount if the user is premium
            if (retrievedUser instanceof PremiumUser) {
                newOrder.setTotalOrderValue(newOrder.getTotalOrderValue() - 10);
            }
            System.out.println("Order created successfully.");

            DriverService.getInstance().assignDriverToOrder(newOrder, driver);

            LockerService.getInstance().addOrderToLocker(newOrder);
        } else {
            System.out.println("USER NOT FOUND. TRY AGAIN!.");
        }
    }


    protected Order getOrderById(int orderId) {
        for (Order order : orderList) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    public static double calculateTotalOrderValue(Order order) {
        double total = 0.0;
        for (Product product : order.getProducts()) {
            total += product.getPrice();
        }

        if (order.getUser() instanceof PremiumUser) {
            total *= 0.9; // 10% discount for premium users
        }

        return total;
    }

    public static void updateOrderTotal(Order order) {
        double totalOrderValue = calculateTotalOrderValue(order);
        order.setTotalOrderValue(totalOrderValue);
    }
}
