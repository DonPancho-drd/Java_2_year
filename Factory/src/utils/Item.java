package utils;

public abstract class Item {
    private final int ID;

    public Item(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}