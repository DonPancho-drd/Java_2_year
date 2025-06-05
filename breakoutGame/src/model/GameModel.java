package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameModel {
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;
    private final List<PowerUp> powerUps;

    private int score;
    private int lives;
    private int level;

    private GameStatus status;
    private boolean movingEvenColumns = true;

    private final int screenWidth;
    private final int screenHeight;

    private static double BASE_SCORE = 10;



    public enum GameStatus {
        PLAYING,
        GAME_OVER,
        GAME_WON
    }

    public GameModel(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.powerUps = new ArrayList<>();
        initGame();
    }

    public void initGame() {
        ball = new Ball(screenWidth / 2, screenHeight - 100, 8, screenWidth);
        paddle = new Paddle(screenWidth / 2 - 50, screenHeight - 30, screenWidth / 6, 10, screenWidth);
        score = 0;
        lives = 3;
        level = 1;
        status = GameStatus.PLAYING;
        initBricks();
    }

    private void initBricks() {
        bricks = new ArrayList<>();

        Random rand = new Random();
        int MAX_LEVEL_FOR_BH = 8;
        int rows = 4 + (Math.min(level, MAX_LEVEL_FOR_BH));
        int cols = 9;

        double moveSpeed =  Math.min(level, 8) * 0.1;

        int padding = screenWidth / 100;
        int offsetTop = screenHeight / 12;
        int offsetLeft = screenWidth / 20;
        int brickWidth = (screenWidth - offsetLeft * 2) / cols - padding;
        int brickHeight = (screenHeight / 3 - offsetTop) / 5 - padding;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double patternChance = rand.nextDouble();
                if (patternChance > 0.2) {
                    Color color = new Color(rand.nextInt(200) + 55, rand.nextInt(200) + 55, rand.nextInt(200) + 55);
                    double x = offsetLeft + j * (brickWidth + padding);
                    double y = offsetTop + i * (brickHeight + padding);
                    Brick brick = new Brick(x, y, brickWidth, brickHeight, color);
                    brick.setColumn(j);

                    // двигающеся бруски

                    if (movingEvenColumns && j % 2 == 0) {
                        brick.setStuck(false);
                        brick.setDy(moveSpeed);
                    }

                    bricks.add(brick);
                    if (rand.nextDouble() < 0.1) {
                        brick.setPowerUp(new PowerUp(brick.getX(), brick.getY(),
                                rand.nextBoolean() ? PowerUp.Type.EXTRA_LIFE : PowerUp.Type.WIDE_PADDLE));
                    }
                }
            }
        }
    }

    public void update() {
        if (status != GameStatus.PLAYING) return;

        ball.move();
        paddle.move();
        for (Brick brick : bricks) {
            brick.move();
        }

        checkCollisions();
        updatePowerUps();
        checkGameStatus();
    }

    private void updatePowerUps() {
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.move();
            if (powerUp.getBounds().intersects(paddle.getBounds())) {
                powerUp.apply(this);
                iterator.remove();
            } else if (powerUp.getY() > screenHeight) {
                iterator.remove();
            }
        }
    }

    private void checkCollisions() {

        // столкновение с платформой
        if (ball.getBounds().intersects(paddle.getBounds()) && ball.getDy() > 0) {
            ball.bounceFromPaddle(paddle.getX(), paddle.getWidth(), paddle.getDx());
        }

        // столкновение с кирпичами
        for (Brick brick : bricks) {
            if (brick.isVisible() && ball.getBounds().intersects(brick.getBounds())) {
                brick.setVisible(false);
                score += BASE_SCORE * level;

                ball.bounceFromBrick(brick.getBounds(),
                        brick.getX() + brick.getWidth() / 2.0,
                        brick.getY() + brick.getHeight() / 2.0);

                if (brick.getPowerUp() != null) {
                    powerUps.add(new PowerUp(brick.getX(), brick.getY(), brick.getPowerUp().getType()));
                }
                break;
            }

            if(brick.isVisible() && brick.getY()+ brick.getHeight() >= screenHeight){
                minusLife();
                break;
            }
        }

        if (ball.getY() + ball.getRadius() > screenHeight) {
            minusLife();
        }
    }

    public void minusLife(){
        lives--;
        if (lives > 0) {
            resetBallAndPaddle();
        }

    }

    public void nextLevel() {
        level++;
        ball.raiseSpeedUpLevel();
        initBricks();
        resetBallAndPaddle();
        setLives(3);
        status = GameStatus.PLAYING;
    }

    private void resetBallAndPaddle() {
        ball.reset(screenWidth / 2, screenHeight - 100);
        paddle.setX(screenWidth / 2 - paddle.getWidth() / 2);
        paddle.setWidth(screenWidth / 6);
        setBricksStuck(true);
    }

    private void checkGameStatus() {
        if (lives <= 0) {
            status = GameStatus.GAME_OVER;
            return;
        }

        boolean allBricksDestroyed = true;
        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                allBricksDestroyed = false;
                break;
            }
        }

        if (allBricksDestroyed) {
            status = GameStatus.GAME_WON;
        }
    }

    public void addLife() {
        lives++;
    }


    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public List<Brick> getBricks() { return bricks; }
    public List<PowerUp> getPowerUps() { return powerUps; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }

    public boolean isGameOver() { return status == GameStatus.GAME_OVER; }
    public boolean isGameWon() { return status == GameStatus.GAME_WON; }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setBricksStuck(boolean b) {
        for(Brick brick: bricks){
            brick.setEnabledStuck(b);
        }
    }
}