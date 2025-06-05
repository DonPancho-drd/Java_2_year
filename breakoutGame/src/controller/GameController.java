package controller;

import javax.swing.*;
import java.awt.event.*;
import model.*;
import view.*;

public class GameController {
    private final GameModel model;
    private final GameView view;
    private final GamePanel gamePanel;
    private final HighScoreManager highScoreManager;
    private Timer timer;

    public static final String HIGH_SCORE_FILE = "highscores.txt";
    public static final int FRAME_RATE = 100; // FPS

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        this.gamePanel = view.getGamePanel();
        this.gamePanel.setModel(model);
        this.highScoreManager = new HighScoreManager(HIGH_SCORE_FILE, view);

        new InputHandler(model, gamePanel, view);
        initialize();
    }

    private void initialize() {
        setupMenuListeners();
        setupGameLoop();
        setupFocus();
    }


    private void setupMenuListeners() {
        view.addMenuListener(view.getNewGameItem(), this::startNewGame);
        view.addMenuListener(view.getHighScoresItem(), highScoreManager::showHighScores);
        view.addMenuListener(view.getAboutItem(), this::showAbout);
        view.addMenuListener(view.getExitItem(), e -> System.exit(0));
    }

    private void setupGameLoop() {
        timer = new Timer(1000 / FRAME_RATE, e -> {

                model.update();
                gamePanel.repaint();
                updateUI();

                if (model.isGameWon()) {
                    timer.stop();
                    handleLevelComplete();
                } else if (model.isGameOver()) {
                    timer.stop();
                    handleGameOver();
                }

        });
    }

    private void setupFocus() {
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    private void updateUI() {
        view.updateStatusBar(model.getScore(), model.getLives(), model.getLevel());
    }


    private void startNewGame(ActionEvent e) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        model.initGame();
        view.clearGameMessage();
        timer.start();
        setupFocus();
        updateUI();
    }

    private void handleLevelComplete() {
            view.showMessage(
                    "Level " + model.getLevel() + " Complete!\nScore: " + model.getScore(),
                    "Level Finished",
                    JOptionPane.INFORMATION_MESSAGE
            );
            view.showLevelCompleteMessage(model.getScore());
            model.nextLevel();
            timer.start();
            setupFocus();
            updateUI();

    }

    private void handleGameOver() {
        view.showGameOverMessage(model.getScore());
        String name = view.showInputDialog("Game Over! Score: " + model.getScore() + "\nEnter your name:", "High Score");
        if (name != null && !name.trim().isEmpty()) {
            highScoreManager.saveHighScore(name.trim(), model.getScore());
        }
        updateUI();
    }

    private void showAbout(ActionEvent e) {
        view.showMessage(
                "Breakout Game\nVersion 2.0\n\nControls:\n← → arrows to move\nSpace to launch",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}