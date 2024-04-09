package orderInfo;

import Delivery.Driver;
import Delivery.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class Order implements Comparable<Order> {
    private int orderId;
    private User user;
    private List<Product> products;
    private String deliveryAddress;
    private String deliveryTime;
    private boolean hasBeenDelivered;
    private Feedback feedback;
    private double totalOrderValue;
    private Warehouse warehouse;
    private Driver driver; // Add Driver field


    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public double getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(double totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }

    // Constructors
    public Order(int orderId, User user, List<Product> products, String deliveryAddress, String deliveryTime, boolean hasBeenDelivered) {
        this.orderId = orderId;
        this.user = user;
        this.products = products;
        this.deliveryAddress = deliveryAddress;
        this.deliveryTime = deliveryTime;
        this.hasBeenDelivered = hasBeenDelivered;
    }

    public Order(int orderId, User user, List<Product> products, String deliveryAddress, String deliveryTime) {
        this(orderId, user, products, deliveryAddress, deliveryTime, false);
    }

    public Order(int orderId) {
        this.orderId = orderId;
        this.products = new ArrayList<>();
    }
    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isHasBeenDelivered() {
        return hasBeenDelivered;
    }

    public void setHasBeenDelivered(boolean hasBeenDelivered) {
        this.hasBeenDelivered = hasBeenDelivered;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
    @Override
    public int compareTo(Order otherOrder) {
        // Compare orders based on their orderId
        return Integer.compare(this.orderId, otherOrder.orderId);
    }

}
