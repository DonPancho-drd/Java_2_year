package factory;

import factory.carparts.Accessory;
import factory.carparts.Body;
import factory.carparts.Engine;
import threadpool.CarAssemblyTask;
import threadpool.ThreadPool;

public class Controller implements Runnable {
    private final ThreadPool threadPool;
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;
    private volatile boolean isRunning;

    public Controller(ThreadPool threadPool, Storage<Body> bodyStorage, Storage<Engine> engineStorage,
                      Storage<Accessory> accessoryStorage, Storage<Car> carStorage,
                      int carStorageCapacity) {
        if (threadPool == null || bodyStorage == null || engineStorage == null ||
                accessoryStorage == null || carStorage == null) {
            throw new IllegalArgumentException("ThreadPool and storages cannot be null");
        }
        if (carStorageCapacity <= 0) {
            throw new IllegalArgumentException("Car storage capacity must be positive");
        }
        this.threadPool = threadPool;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            try {
                synchronized (carStorage) {
                    while(!isRunning || !needsMoreCars()) {
                        carStorage.wait();
                    }
                    CarAssemblyTask task = new CarAssemblyTask(bodyStorage, engineStorage,
                            accessoryStorage, carStorage);
                    threadPool.submit(task);
                    carStorage.notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                break;
            } catch (Exception e) {
                System.err.println("Error in Controller: " + e.getMessage());
            }
        }
    }

    private boolean needsMoreCars() {
        return carStorage.getStorageSize() + threadPool.getTasksInProcess() + threadPool.getQueueSize() < carStorage.getStorageCapacity();
    }


    public void stop() {
        isRunning = false;
        synchronized (this) {
            notifyAll();
        }
    }
}