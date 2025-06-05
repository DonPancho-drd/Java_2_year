package factory.staff;


import factory.Car;
import factory.Logger;
import factory.Storage;


public class Dealer implements Runnable {
    private final Storage<Car> carStorage;
    private final Logger logger;
    private final int dealerNumber;
    private int timeInterval;
    private volatile boolean isRunning;


    public Dealer(Storage<Car> carStorage, Logger logger, int dealerNumber, int timeInterval) {
        if (carStorage == null || logger == null) {
            throw new IllegalArgumentException("Car storage and logger cannot be null");
        }
        if (timeInterval <= 0) {
            throw new IllegalArgumentException("Request interval must be positive");
        }
        this.carStorage = carStorage;
        this.logger = logger;
        this.dealerNumber = dealerNumber;
        this.timeInterval = timeInterval;
        this.isRunning = true;
    }


    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                Car car;
//                synchronized (carStorage) {
//                    while (carStorage.isEmpty() && isRunning) {
//                        carStorage.wait();
//                    }
                    if (!isRunning) {
                        break;
                    }
                    car = carStorage.take();
//                    carStorage.notifyAll();
//                }

                logger.logSale(dealerNumber, car);
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                break;
            } catch (Exception e) {
                System.err.println("Error in Dealer " + dealerNumber + ": " + e.getMessage());
            }
        }
    }

    public void stop() {
        isRunning = false;
        synchronized (carStorage) {
            carStorage.notifyAll();
        }
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }
}