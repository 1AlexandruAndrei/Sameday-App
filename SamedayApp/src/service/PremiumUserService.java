package service;
import orderInfo.*;
import exception.InvalidDataException;

import java.util.ArrayList;
import java.util.List;

public class PremiumUserService {
    private static final List<PremiumUser> premiumUserList = new ArrayList<>();
    private static int nextUserId = 1;

    public static List<PremiumUser> getPremiumUserList() {
        return premiumUserList;
    }

    public static void addPremiumUser(String username, String password, String email, String phoneNumber) throws InvalidDataException {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            throw new InvalidDataException("Username, password, email, or phone number cannot be empty.");
        }
        PremiumUser premiumUser = new PremiumUser(nextUserId++, username, password, email, phoneNumber);
        premiumUserList.add(premiumUser);
        System.out.println("Premium user added successfully");
    }
}
