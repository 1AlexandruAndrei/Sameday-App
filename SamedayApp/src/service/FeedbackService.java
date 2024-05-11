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

}
