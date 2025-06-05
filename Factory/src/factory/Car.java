package factory;

import factory.carparts.Accessory;
import factory.carparts.Body;
import factory.carparts.Engine;
import utils.Item;

public class Car extends Item {
    private Engine engine;
    private Body body;
    private Accessory accessory;

    public Car(int id, Body body, Engine engine, Accessory accessory) {
        super(id);

        if (body == null || engine == null || accessory == null) {
            throw new IllegalArgumentException("Car parts cannot be null");
        }
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
    }
    public Engine getEngine() { return engine; }
    public Body getBody() { return body; }
    public Accessory getAccessory() { return accessory; }

    @Override
    public int getID() { return super.getID(); }

}
