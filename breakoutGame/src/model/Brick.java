package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import utils.Movable;
import utils.Paintable;

public class Brick implements Paintable, Movable {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private final double width, height;
    private final Color color;
    private boolean visible;
    private PowerUp powerUp;
    private boolean isStuck = true;
    private boolean disabledStuck = true;
    private final double DEFAULT_SPEED = 3.0;

    private int column = -1;

    public Brick(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.visible = true;
        this.dx = 0;
        this.dy = 0;
    }

    @Override
    public void move() {
        if (!isStuck && visible && !disabledStuck) {
            x += dx;
            y += dy;
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)width, (int)height);
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    @Override
    public void paint(Graphics2D g2d) {
        if (visible) {
            g2d.setColor(color);
            g2d.fillRect((int)x, (int)y, (int)width, (int)height);
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getColor() { return color; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setStuck(boolean stuck) { this.isStuck = stuck; }
    public void setY(double y) { this.y = y; }

    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) {this.dy = dy;}
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }

    public boolean isStuck() {return isStuck;
    }

    public void setEnabledStuck(boolean b) {
        this.disabledStuck = b;
    }
}