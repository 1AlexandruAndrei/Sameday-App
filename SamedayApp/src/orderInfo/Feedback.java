package orderInfo;

public class Feedback {
    private int FeedbackId;
    private Order order;
    private int stars;
    private String comment;

    // Constructor
    public Feedback(Order order, int stars, String comment) {
        this.order = order;
        this.stars = stars;
        this.comment = comment;
    }

    // Getters and Setters
    public Order getOrder() {
        return order;
    }

    public int getFeedbackId() {
        return FeedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        FeedbackId = feedbackId;
    }

    public Feedback(int feedbackId, Order order, int stars, String comment) {
        FeedbackId = feedbackId;
        this.order = order;
        this.stars = stars;
        this.comment = comment;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
