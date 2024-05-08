package service;

import orderInfo.Feedback;
import orderInfo.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FeedbackService {
    private static FeedbackService instance;
    private final List<Feedback> feedbackList = new ArrayList<>();
    private int nextFeedbackId = 1;

    private FeedbackService() {}

    public static FeedbackService getInstance() {
        if (instance == null) {
            instance = new FeedbackService();
        }
        return instance;
    }

    public void addFeedbackToOrder(int orderId, int stars, String comment) {
        Order order = OrderService.getInstance().getOrderById(orderId);
        if (order != null) {
            if (stars >= 1 && stars <= 5) {
                Feedback feedback = new Feedback(order, stars, comment);
                order.setFeedback(feedback);
                feedbackList.add(feedback);
            } else {
                System.out.println("Stars should be between 1 and 5.");
            }
        } else {
            System.out.println("ORDER ID INCORRECT! (Orders start from ID: 1)");
        }
    }

    public void displayFeedbackForOrder(int orderId) {
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

    public void provideFeedbackForOrders(Scanner scanner) {
        List<Order> orderList = OrderService.getInstance().getOrderList();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            System.out.println("Provide feedback for Order " + (i + 1) + ":");
            int stars;
            do {
                System.out.print("Stars (1-5): ");
                stars = scanner.nextInt();
                scanner.nextLine();
                if (stars < 1 || stars > 5) {
                    System.out.println("Stars should be between 1 and 5.");
                }
            } while (stars < 1 || stars > 5);
            System.out.print("Comment: ");
            String comment = scanner.nextLine();
            System.out.println();

            addFeedbackToOrder(order.getOrderId(), stars, comment);
        }
    }
}
