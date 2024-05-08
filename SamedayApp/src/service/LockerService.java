package service;

import Delivery.Locker;
import orderInfo.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class LockerService {
    private static final LockerService instance = new LockerService();
    private static final List<Locker> lockerList = new ArrayList<>();
    private static int nextLockerId = 1;
    private static final Scanner scanner = new Scanner(System.in);

    private LockerService() {
    }

    public static LockerService getInstance() {
        return instance;
    }

    public List<Locker> getLockerList() {
        return lockerList;
    }

    public void addLocker(int size, boolean available, String location) {
        Locker locker = new Locker(nextLockerId++, location, size, available);
        lockerList.add(locker);
        System.out.println("Locker added: " + locker.getLockerId());
    }

    public Locker getLockerById(int lockerId) {
        for (Locker locker : lockerList) {
            if (locker.getLockerId() == lockerId) {
                return locker;
            }
        }
        return null;
    }

    public void addOrderToLocker(Order order) {
        System.out.println("Order size: " + order.getProducts().size());

        boolean orderAdded = false;
        while (!orderAdded) {
            for (Locker locker : lockerList) {
                if (locker.isAvailable() && locker.getSize() >= order.getProducts().size()) {
                    System.out.println("Locker near you that is available: " + locker.getLockerId() + locker.getLocation());
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

    public void displayLockers() {
        if (lockerList.isEmpty()) {
            System.out.println("No lockers available.");
        } else {
            for (Locker locker : lockerList) {
                System.out.println("Locker ID: " + locker.getLockerId());
                System.out.println("Location: " + locker.getLocation());
                System.out.println("Size: " + locker.getSize());
                System.out.println("Available: " + locker.isAvailable());
                System.out.println("-----------------------------------");
            }
        }
    }

    public void deleteLockerById(int lockerId) {
        Iterator<Locker> iterator = lockerList.iterator();
        while (iterator.hasNext()) {
            Locker locker = iterator.next();
            if (locker.getLockerId() == lockerId) {
                iterator.remove();
                System.out.println("Locker with ID " + lockerId + " has been deleted.");
                return;
            }
        }
        System.out.println("No locker found with ID " + lockerId);
    }

    public void updateLocker(int lockerId, int newSize, boolean newAvailable, String newLocation) {
        for (Locker locker : lockerList) {
            if (locker.getLockerId() == lockerId) {
                locker.setSize(newSize);
                locker.setAvailable(newAvailable);
                locker.setLocation(newLocation);
                System.out.println("Locker with ID " + lockerId + " has been updated.");
                return;
            }
        }
        System.out.println("No locker found with ID " + lockerId);
    }

    public void addLockerFromInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter locker location: ");
        String lockerLocation = scanner.nextLine();
        System.out.print("Enter locker size: ");
        int lockerSize = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Is the locker available? (true/false): ");
        boolean lockerAvailable = scanner.nextBoolean();
        LockerService.getInstance().addLocker(lockerSize, lockerAvailable, lockerLocation);
        scanner.close();
    }

}
