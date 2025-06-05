package factory;

import factory.carparts.*;
import factory.staff.Dealer;
import factory.staff.Supplier;
import gui.FactoryGUI;
import threadpool.ThreadPool;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Factory {
    private final ConfigReader config;
    private final Logger logger;
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;
    private final ThreadPool threadPool;
    private final Controller controller;
    private final Supplier<Body> bodySupplier;
    private final Supplier<Engine> engineSupplier;
    private final List<Supplier<Accessory>> accessorySuppliers;
    private final List<Dealer> dealers;
    private volatile boolean isRunning;
    private FactoryGUI gui;

    public Factory(ConfigReader config) throws IOException {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
        this.config = config;
        this.isRunning = true;
        logger = new Logger(config.isLog(), "factory_sales.log");

        bodyStorage = new Storage<>(config.getStorageBodySize());
        engineStorage = new Storage<>(config.getStorageEngineSize());
        accessoryStorage = new Storage<>(config.getStorageAccessorySize());
        carStorage = new Storage<>(config.getStorageCarSize());


        threadPool = new ThreadPool(config.getWorkers());

        controller = new Controller(threadPool, bodyStorage, engineStorage,
                accessoryStorage, carStorage, config.getStorageCarSize());
        new Thread(controller).start();

        bodySupplier = new Supplier<>(bodyStorage, 1000, Body.class);
        new Thread(bodySupplier).start();
        engineSupplier = new Supplier<>(engineStorage, 1000, Engine.class);
        new Thread(engineSupplier).start();
        accessorySuppliers = new ArrayList<>();
        for (int i = 0; i < config.getAccessorySuppliers(); i++) {
            Supplier<Accessory> supplier = new Supplier<>(accessoryStorage, 1000, Accessory.class);
            accessorySuppliers.add(supplier);
            new Thread(supplier).start();
        }

        dealers = new ArrayList<>();
        for (int i = 0; i < config.getDealers(); i++) {
            Dealer dealer = new Dealer(carStorage, logger, i + 1, 2000);
            dealers.add(dealer);
            new Thread(dealer).start();
        }
    }


    public void shutdown() {
        isRunning = false;

        controller.stop();
        System.out.println("Controller stopped");

        threadPool.shutdown();
        System.out.println("ThreadPool stopped");

        bodySupplier.stop();
        System.out.println("Body supplier stopped");

        engineSupplier.stop();
        System.out.println("Engine supplier stopped");

        for (Supplier<Accessory> supplier : accessorySuppliers) {
            supplier.stop();
            System.out.println("Accessory supplier stopped");
        }

        for (Dealer dealer : dealers) {
            dealer.stop();
            System.out.println("Dealer stopped");
        }

        logger.close();
        System.out.println("Logger closed");

        System.out.println("Factory shutdown complete");
        System.exit(0);

    }

    public void setGui(FactoryGUI gui) {
        this.gui = gui;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getBodyStorageSize() {
        return bodyStorage.getStorageSize();
    }

    public int getEngineStorageSize() {
        return engineStorage.getStorageSize();
    }

    public int getAccessoryStorageSize() {
        return accessoryStorage.getStorageSize();
    }

    public int getCarStorageSize() {
        return carStorage.getStorageSize();
    }

    public int getTaskQueueSize() {
        return threadPool.getQueueSize();
    }

    public int getTasksCompletedByWorkers() {
        return threadPool.getTasksCompletedByWorkers();
    }

    public int getTasksInProcess() {
        return threadPool.getTasksInProcess();
    }

    public int getBodiesProduced() {
        return bodyStorage.getItemsProduced();
    }

    public int getEnginesProduced() {
        return engineStorage.getItemsProduced();
    }

    public int getAccessoriesProduced() {
        return accessoryStorage.getItemsProduced();
    }

    public int getCarsProduced() {
        return carStorage.getItemsProduced();
    }

    public void setBodySupplierSpeed(int speed) {
        bodySupplier.setTimeInterval(speed);
    }

    public void setEngineSupplierSpeed(int speed) {
        engineSupplier.setTimeInterval(speed);
    }

    public void setAccessorySupplierSpeed(int speed) {
        for (Supplier<Accessory> supplier : accessorySuppliers) {
            supplier.setTimeInterval(speed);
        }
    }

    public void setDealerSpeed(int speed) {
        for (Dealer dealer : dealers) {
            dealer.setTimeInterval(speed);
        }
    }
}