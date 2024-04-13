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

import static service.DriverService.assignDriverToOrder;
import static service.LockerService.addOrderToLocker;
import static service.ProductService.getProductById;
import static service.ProductService.getProductList;
import static service.UserService.getUserById;

public class OrderService {
    private static final List<Order> orderList = new ArrayList<>();
    private static int nextOrderId = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static List<Order> getOrderList() {
        return orderList;
    }

    public static void createOrder(User user, String deliveryAddress, String deliveryTime, Driver driver) {
        List<Product> selectedProducts = new ArrayList<>();
        int productId;

        System.out.println("Available Products:");
        for (Product product : getProductList()) {
            System.out.println(product.getProductId() + ". " + product.getName() + " ($" + product.getPrice() + ")");
        }

        System.out.println("Select products from the list below to add to the order (press 0 when finished):");
        while (true) {
            try {
                productId = scanner.nextInt();
                if (productId == 0) {
                    break;
                }
                Product selectedProduct = getProductById(productId);
                if (selectedProduct != null) {
                    selectedProducts.add(selectedProduct);
                } else {
                    System.out.println("INVALID INPUT! PRESS 0 TO FINISH OR A VALID PRODUCT ID.");
                }
            } catch (InputMismatchException e) {
                System.out.println("INVALID INPUT! PRESS 0 TO FINISH OR A VALID PRODUCT ID.");
                scanner.next();
            }
        }

        if (selectedProducts.isEmpty()) {
            System.out.println("NO PRODUCTS SELECTED. THE ORDER WAS NOT CREATED.");
            return;
        }

        User retrievedUser = getUserById(user.getUserId());
        if (retrievedUser != null) {
            Order newOrder = new Order(nextOrderId++, retrievedUser, selectedProducts, deliveryAddress, deliveryTime);
            orderList.add(newOrder);
            updateOrderTotal(newOrder);

            // - 10 USD discount if the user is premium
            if (retrievedUser instanceof PremiumUser) {
                newOrder.setTotalOrderValue(newOrder.getTotalOrderValue() - 10);
            }
            System.out.println("Order created successfully.");

            assignDriverToOrder(newOrder, driver); // Assign driver to the order

            addOrderToLocker(newOrder);
        } else {
            System.out.println("USER NOT FOUND. TRY AGAIN!.");
        }
    }



    protected static Order getOrderById(int orderId) {
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

        // Check if the user is a premium user and apply discount if true
        if (order.getUser() instanceof PremiumUser) {
            total *= 0.9; // Apply 10% discount for premium users
        }

        return total;
    }


    public static void updateOrderTotal(Order order) {
        double totalOrderValue = calculateTotalOrderValue(order);
        order.setTotalOrderValue(totalOrderValue);
    }
}