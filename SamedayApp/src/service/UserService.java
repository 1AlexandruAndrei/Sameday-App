
package service;

import orderInfo.*;

import java.util.*;
import exception.InvalidDataException;

public class UserService {
    private static final List<User> userList = new ArrayList<>();
    private static int nextUserId = 1;

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

    protected static User getUserById(int userId) {
        for (User user : userList) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public static void displayUser(User user) {
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