import Delivery.*;
import orderInfo.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Service {
    private static final List<User> userList = new ArrayList<>();
    private static final List<Order> orderList = new ArrayList<>();
    private static final List<Product> productList = new ArrayList<>();
    private static final List<Feedback> feedbackList = new ArrayList<>();
    private static final List<Locker> lockerList = new ArrayList<>();
    private static final List<Warehouse> warehouseList = new ArrayList<>();


    private static int nextUserId = 1;
    private static int nextOrderId = 1;
    private static int nextProductId = 1;
    private static int nextFeedbackId = 1;
    private static int nextLockerId = 1;

    protected static Scanner scanner = new Scanner(System.in);

    public static List<User> getUserList() {
        return userList;
    }

    public static List<Product> getProductList() {
        return productList;
    }

    public static List<Locker> getLockerList() {
        return lockerList;
    }
    public static List<Order> getOrderList() {
        return orderList;
    }

    ///////////////////// USER /////////////////////


    protected static void createUser(String username, String password, String email, String phoneNumber, boolean isPremium) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Username, password, email, or phone number cannot be empty.");
        }
        if (isPremium) {
            addPremiumUser(username, password, email, phoneNumber);
        } else {
            addUser(username, password, email, phoneNumber);
        }
    }

    public static void addPremiumUser(String username, String password, String email, String phoneNumber) {
        PremiumUser premiumUser = new PremiumUser(nextUserId++, username, password, email, phoneNumber);
        userList.add(premiumUser);
        System.out.println("Premium user added successfully");
    }

    public static void addUser(String username, String password, String email, String phoneNumber) {
        User user = new User(nextUserId++, username, password, email, phoneNumber);
        userList.add(user);
        System.out.println("User added successfully");
    }

    protected static User getUserById(int userId) {
        for (User user : userList) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public static void displayUser(User user) {
        System.out.println("User ID: " + user.getUserId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Phone Number: " + user.getPhoneNumber());

        if (user instanceof PremiumUser) {
            System.out.println("Premium User: Yes");
        } else {
            System.out.println("Premium User: No");
        }

        System.out.println();
    }

    ///////////////////// ORDER /////////////////////

    public static void createOrder(User user, List<Product> products, String deliveryAddress, String deliveryTime, Driver driver) {
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
            System.out.println("No products selected. The order was not created.");
            return;
        }

        User retrievedUser = getUserById(user.getUserId());
        if (retrievedUser != null) {
            Order newOrder = new Order(nextOrderId++, retrievedUser, selectedProducts, deliveryAddress, deliveryTime);
            orderList.add(newOrder);
            updateOrderTotal(newOrder);

            driver.assignOrder(newOrder);

            // - 10 USD discount if the user is premium
            if (retrievedUser instanceof PremiumUser) {
                newOrder.setTotalOrderValue(newOrder.getTotalOrderValue() - 10);
            }
            System.out.println("Order created successfully.");

            addOrderToLocker(newOrder);
        } else {
            System.out.println("USER NOT FOUND. TRY AGAIN!.");
        }
    }


    private static Order getOrderById(int orderId) {
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

    ///////////////////// PRODUCT /////////////////////

    private static Product getProductById(int productId) {
        for (Product product : productList) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public static void createProduct(String name, double price) {
        productList.add(new Product(nextProductId++, name, price));
    }

    ///////////////////// LOCKER /////////////////////

    public static void addLocker(int size, boolean available) {
        Locker locker = new Locker(nextLockerId++, "Location", size, available);
        lockerList.add(locker);
        System.out.println("Locker added: " + locker.getLockerId());
    }

    public static Locker getAvailableLocker(int orderSize) {
        for (Locker locker : lockerList) {
            if (locker.isAvailable() && orderSize <= locker.getSize()) {
                return locker;
            }
        }
        return null;
    }

    public static void addOrderToLocker(Order order) {
        System.out.println("Order size: " + order.getProducts().size());

        boolean orderAdded = false;
        while (!orderAdded) {
            for (Locker locker : lockerList) {
                if (locker.isAvailable() && locker.getSize() >= order.getProducts().size()) {
                    System.out.println("Locker available for your order: " + locker.getLockerId());
                    System.out.println("Do you want to use this locker? (1 for yes, 0 for no): ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice == 1) {
                        System.out.println("Order added to locker no. " + locker.getLockerId());
                        locker.setSize(locker.getSize() - 1);
                        if (locker.getSize() == 0) {
                            locker.setAvailable(false);
                        }
                        orderAdded = true;
                        break;
                    }
                }
            }

            if (!orderAdded) {
                System.out.println("No suitable locker available for your order. Do you want to proceed without a locker? (1 for yes, 0 for no): ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    System.out.println("Order will be processed without a locker.");
                    break;
                }
            }
        }
    }


    ///////////////////// FEEDBACK /////////////////////

    public static void addFeedbackToOrder(int orderId, int stars, String comment) {
        Order order = getOrderById(orderId);
        if (order != null) {
            Feedback feedback = new Feedback(order, stars, comment);
            order.setFeedback(feedback);
            feedbackList.add(feedback);
        } else {
            System.out.println("ORDER ID INCORRECT! (Orders start from ID: 1)");
        }
    }

    public static void displayFeedbackForOrder(int orderId) {
        for (Feedback feedback : feedbackList) {
            if (feedback.getOrder().getOrderId() == orderId) {
                System.out.println("--------------------------");
                System.out.println("Order ID: " + orderId);
                System.out.println("Your rate to our services: " + feedback.getStars());
                System.out.println("Your comment: " + feedback.getComment());
                System.out.println("--------------------------");
                return;
            }
        }
        System.out.println("No feedback found for Order with ID " + orderId);
    }
    public static void provideFeedbackForOrders(Scanner scanner) {
        List<Order> orderList = getOrderList();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            System.out.println("Provide feedback for Order " + (i + 1) + ":");
            System.out.print("Stars (1-5): ");
            int stars = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Comment: ");
            String comment = scanner.nextLine();
            System.out.println();

            addFeedbackToOrder(order.getOrderId(), stars, comment);
        }
    }


    ///////////////////// DRIVER /////////////////////

    public static void assignDriverToOrder(Order order, Driver driver) {
        driver.assignOrder(order);
        System.out.println("Order assigned to " + driver.getName());
    }

    ///////////////////// WAREHOUSE /////////////////////

    public static void addWarehouse(String location, int capacity) {
        Warehouse warehouse = new Warehouse(generateWarehouseId(), location, capacity);
        warehouseList.add(warehouse);
        System.out.println("Warehouse added: " + warehouse.getWarehouseId());
    }

    public static void assignOrderToWarehouse(Order order) {
        for (Warehouse warehouse : warehouseList) {
            if (order.getProducts().size() <= warehouse.getCapacity()) {
                warehouse.addOrder(order);
                return;
            }
        }
        System.out.println("No suitable warehouse available for the order.");
    }

    private static int generateWarehouseId() {
        // Logic to generate a unique warehouse ID
        return warehouseList.size() + 1;
    }

    public static void displayDriver(Driver driver) {
        System.out.println("The delivery driver assigned to your order is " + driver.getName());
        System.out.println("ID of the orders that will be delivered by " + driver.getName() + " are: ");
        for (orderInfo.Order order : driver.getActiveOrders()) {
            System.out.println(order.getOrderId());
            // Add more order details if needed
        }
        System.out.println();
    }

    public static void displayWarehouse(Warehouse warehouse) {
        System.out.println("--------------------------");
        System.out.println("Warehouse ID: " + warehouse.getWarehouseId());
        System.out.println("Location: " + warehouse.getLocation());
        System.out.println("Capacity: " + warehouse.getCapacity());
        System.out.println("--------------------------");
        System.out.println();
    }


}
