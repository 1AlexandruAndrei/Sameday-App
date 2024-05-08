package service;

import exception.InvalidDataException;
import orderInfo.PremiumUser;
import orderInfo.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static UserService instance;
    private static final List<User> userList = new ArrayList<>();
    private static int nextUserId = 1;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void createUser(String username, String password, String email, String phoneNumber, boolean isPremium)
            throws InvalidDataException {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            throw new InvalidDataException("Username, password, email, or phone number cannot be empty.");
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

    protected User getUserById(int userId) {
        for (User user : userList) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public void displayUser(User user) {
        System.out.println("Your account ID: " + user.getUserId());
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
}
