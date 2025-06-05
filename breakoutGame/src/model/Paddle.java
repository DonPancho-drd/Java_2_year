package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.Movable;
import utils.Paintable;

public class Paddle implements Paintable, Movable {
    private double x;
    private final double y;
    private int width;
    private final int height;
    private double dx;
    private final int imageWidth;

    private final int DEFAULT_SPEED = 10;

    public Paddle(int x, int y, int width, int height, int imageWidth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = 0;
        this.imageWidth = imageWidth;
    }

    @Override
    public void move() {
        x += dx;
        if (x < 0) x = 0;
        if (x > imageWidth - width) x = imageWidth - width;
    }


    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.fillRect((int)x, (int)y, width, height);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public void setWidth(int width) {
        this.width = Math.min(width, 200);
    }

    public void setDefaultDx(int direction){
        setDx( DEFAULT_SPEED*direction );
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getDx() { return dx; }
    public void setX(int x) { this.x = x; }
    public void setDx(int dx) { this.dx = dx; }
}