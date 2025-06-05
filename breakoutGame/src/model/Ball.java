package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.Movable;
import utils.Paintable;

public class Ball implements Paintable, Movable {
    private double x, y;
    private final int radius;
    private double dx, dy;
    private boolean isStuck = true;
    private double speedIncrease;
    private static final double DEFAULT_SPEED = 4;
    private double maxSpeed;
    private int screenWidth;
    private static final double MAXIMUM_SPEED = 12;
    private static final double MIN_VERTICAL = 3.0;
    private static final double MAX_ANGLE = Math.toRadians(80);

    public Ball(int x, int y, int radius, int screenWidth) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 4;
        this.dy = -4;
        this.speedIncrease = 1;
        this.maxSpeed = 8;
        this.screenWidth = screenWidth;
    }
    @Override
    public void move() {
        if (!isStuck) {
            x += Math.signum(dx) * Math.min(Math.abs(dx * speedIncrease), maxSpeed / Math.sqrt(2));
            y += Math.signum(dy) * Math.min(Math.abs(dy * speedIncrease), maxSpeed / Math.sqrt(2));
        }
        checkCollisions();

    }

    public void checkCollisions() {
        // столкновение со стенками
        if (this.getX() - this.getRadius() <= 0) {
            this.setX(this.getRadius());
            this.bounceHorizontal();
        }

        if (this.getX() + this.getRadius() >= screenWidth) {
            this.setX(screenWidth - this.getRadius());
            this.bounceHorizontal();
        }

        if (this.getY() - this.getRadius() <= 0) {
            this.setY(this.getRadius());
            this.bounceVertical();
        }
    }

    public void bounceHorizontal() {
        dx = -dx;
    }

    public void bounceVertical() {
        dy = -dy;
    }

    public void bounceFromPaddle(double paddleX, int paddleWidth, double paddleSpeed) {
        double hitPosition = (x - (paddleX + paddleWidth / 2.0)) / (paddleWidth / 2.0);
        hitPosition = Math.max(-1, Math.min(1, hitPosition));

        double angle = hitPosition * MAX_ANGLE;
        double baseSpeed = Math.sqrt(dx * dx + dy * dy);
        double speed = Math.min(baseSpeed, maxSpeed);

        dx = speed * Math.sin(angle) + paddleSpeed * 0.2;
        dy = -Math.max(MIN_VERTICAL, speed * Math.cos(angle));

        double finalSpeed = Math.sqrt(dx * dx + dy * dy);
        if (finalSpeed > maxSpeed) {
            double ratio = maxSpeed / finalSpeed;
            dx *= ratio;
            dy *= ratio;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    }

    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = DEFAULT_SPEED * (Math.random() > 0.5 ? 1 : -1);
        this.dy = -DEFAULT_SPEED;
        this.isStuck = true;
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public int getRadius() { return radius; }
    public int getDx() { return (int) dx; }
    public int getDy() { return (int) dy; }
    public boolean isStuck() { return isStuck; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
    public void setStuck(boolean stuck) { this.isStuck = stuck; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void raiseSpeedUpLevel() {
        speedIncrease *= 1.15;
        maxSpeed = Math.min(maxSpeed * 1.08, MAXIMUM_SPEED);
    }

    public void bounceFromBrick(Rectangle brickBounds, double brickCenterX, double brickCenterY) {
        Rectangle ballBounds = this.getBounds();
        double ballCenterX = this.getX();
        double ballCenterY = this.getY();

        double width = (ballBounds.width + brickBounds.width) / 2.0;
        double height = (ballBounds.height + brickBounds.height) / 2.0;

        double absDx = Math.abs(ballCenterX - brickCenterX);
        double absDy = Math.abs(ballCenterY - brickCenterY);

        double ratioDiff = Math.abs((absDx / width) - (absDy / height));

        if (ratioDiff < 0.15) {
            // удар по углу
            this.bounceHorizontal();
            this.bounceVertical();
        } else if (absDx / width > absDy / height) {
            // удар сбоку
            this.bounceHorizontal();
        } else {
            // удар сверху/снизу
            this.bounceVertical();
        }
    }
}