package factory.staff;

import factory.Storage;
import factory.carparts.Part;

public class Supplier<T extends Part> implements Runnable {
    private final Storage<T> storage;
    private int timeInterval;
    private final Class<T> partType;
    private volatile boolean isRunning;
    private static int partIdCounter = 0;

    public Supplier(Storage<T> storage, int timeInterval, Class<T> partType) {
        if (storage == null || partType == null) {
            throw new IllegalArgumentException("Storage and item type cannot be null");
        }
        if (timeInterval <= 0) {
            throw new IllegalArgumentException("Supply interval must be positive");
        }
        this.storage = storage;
        this.timeInterval = timeInterval;
        this.partType = partType;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                T item = createItem();
//                synchronized (storage) {
//                    while (storage.isFull() && isRunning) {
//                        storage.wait();
//                    }
//                    if (!isRunning) {
//                        break;
//                    }
                    storage.put(item);
//                    storage.notifyAll();
//                }
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                break;
            } catch (Exception e) {
                System.err.println("Error in Supplier for " + partType.getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    private T createItem() {
        try {
            synchronized (Supplier.class) {
                T item = partType.getDeclaredConstructor(int.class).newInstance(partIdCounter++);
                return item;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create item of type " + partType.getSimpleName(), e);
        }
    }

    public void stop() {
        isRunning = false;
        synchronized (storage) {
            storage.notifyAll();
        }
    }

    public void setTimeInterval(int timeInterval) {
        if (timeInterval <= 0) {
            throw new IllegalArgumentException("Supply interval must be positive");
        }
        this.timeInterval = timeInterval;
    }
}