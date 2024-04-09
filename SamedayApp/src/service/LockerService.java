package service;

import Delivery.*;
import orderInfo.*;

import java.util.*;

public class LockerService {
    private static final List<Locker> lockerList = new ArrayList<>();
    private static int nextLockerId = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static List<Locker> getLockerList() {
        return lockerList;
    }

    public static void addLocker(int size, boolean available) {
        Locker locker = new Locker(nextLockerId++, "Location", size, available);
        lockerList.add(locker);
        System.out.println("Locker added: " + locker.getLockerId());
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

}
