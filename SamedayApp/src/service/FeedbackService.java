package service;

import orderInfo.Feedback;
import orderInfo.Order;

import java.util.ArrayList;
import java.util.List;

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


}
