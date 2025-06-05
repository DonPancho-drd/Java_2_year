package threadpool;


import factory.Car;
import factory.Storage;
import factory.carparts.*;


public class CarAssemblyTask implements Task {
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;
    private static int carIdCounter = 0;

    public CarAssemblyTask(Storage<Body> bodyStorage, Storage<Engine> engineStorage,
                           Storage<Accessory> accessoryStorage, Storage<Car> carStorage) {
        if (bodyStorage == null || engineStorage == null || accessoryStorage == null ||
                carStorage == null) {
            throw new IllegalArgumentException("Storages cannot be null");
        }
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;


    }


    @Override
    public void execute() {
        try {
            Body body;
            Engine engine;
            Accessory accessory;

            body = bodyStorage.take();
            engine = engineStorage.take();
            accessory = accessoryStorage.take();

            Car car = new Car(getNextCarId(), body, engine, accessory);
            carStorage.put(car);


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("CarAssemblyTask " + " interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error in CarAssemblyTask " + ": " + e.getMessage());
        }
    }

    private synchronized static int getNextCarId() {
        return carIdCounter++;
    }
}