package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.Movable;
import utils.Paintable;

public class PowerUp implements Paintable, Movable {
    private final double x;
    private double y;
    private final int width = 20, height = 20;
    private final Type type;
    private final double dy = 2;

    public enum Type {
        EXTRA_LIFE, WIDE_PADDLE
    }

    public PowerUp(double x, double y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public void move() {
        y += dy;
    }

    public void apply(GameModel model) {
        switch (type) {
            case EXTRA_LIFE:
                model.addLife();
                break;
            case WIDE_PADDLE:
                model.getPaddle().setWidth(model.getPaddle().getWidth() + 20);
                break;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(type == Type.EXTRA_LIFE ? Color.YELLOW : Color.BLUE);
        g2d.fillRect((int)x, (int)y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Type getType() { return type; }
}