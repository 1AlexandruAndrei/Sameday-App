package orderInfo;

public class PremiumUser extends User {
    private boolean premiumStatus;
    private int loyaltyPoints;

    public PremiumUser(int userId, String username, String password, String email, String phoneNumber) {
        super(userId, username, password, email, phoneNumber);
    }

    public PremiumUser(String username, String password, String email, String phoneNumber) {
        super();
    }


    public boolean isPremiumStatus() {
        return premiumStatus;
    }

    public void setPremiumStatus(boolean premiumStatus) {
        this.premiumStatus = premiumStatus;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    @Override
    public void displayUserDetails() {
        super.displayUserDetails(); //superclass method
        System.out.println("Premium User: Yes");
    }
}
