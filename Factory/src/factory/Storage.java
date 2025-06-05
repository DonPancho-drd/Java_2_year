package factory;

import utils.Item;

import java.util.ArrayList;

public class Storage<T extends Item> {
    private final int storageCapacity;
    private int itemsProduced = 0;
    private final ArrayList<T> arrayOfItems;


    public Storage(int storageCapacity) {
        if (storageCapacity <= 0) {
            throw new IllegalArgumentException("Storage size must be positive");
        }
        this.storageCapacity = storageCapacity;
        this.arrayOfItems = new ArrayList<>(storageCapacity);
    }

    public synchronized boolean isEmpty() {
        return arrayOfItems.isEmpty();
    }

    public synchronized boolean isFull() {
        return arrayOfItems.size() == storageCapacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        arrayOfItems.add(item);
        itemsProduced++;
        notifyAll();
    }


    public synchronized T take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        T item = arrayOfItems.remove(0);
        notifyAll();
        return item;
    }

    public synchronized int getStorageSize(){
        return arrayOfItems.size();
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public synchronized int getItemsProduced() {
        return itemsProduced;
    }
}