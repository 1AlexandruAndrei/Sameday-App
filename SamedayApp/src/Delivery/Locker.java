package Delivery;

public class Locker {
    private int lockerId;
    private String location;
    private int size;
    private boolean available;

    public Locker(int lockerId, String location, int size, boolean available) {
        this.lockerId = lockerId;
        this.location = location;
        this.size = size;
        this.available = available;
    }

    public int getLockerId() {
        return lockerId;
    }

    public void setLockerId(int lockerId) {
        this.lockerId = lockerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
