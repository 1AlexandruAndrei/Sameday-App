package service;

import orderInfo.*;

import java.util.*;

import static service.OrderService.getOrderById;
import static service.OrderService.getOrderList;

public class FeedbackService {
    private static final List<Feedback> feedbackList = new ArrayList<>();
    private static int nextFeedbackId = 1;

    public static void addFeedbackToOrder(int orderId, int stars, String comment) {
        Order order = getOrderById(orderId);
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
