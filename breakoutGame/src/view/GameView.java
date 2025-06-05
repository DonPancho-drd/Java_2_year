package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class GameView {
    private final JFrame frame;
    private final GamePanel gamePanel;
    private final JMenuItem newGameItem;
    private final JMenuItem highScoresItem;
    private final JMenuItem aboutItem;
    private final JMenuItem exitItem;
    private final JLabel statusBar;

    public GameView(int width, int height) {
        frame = new JFrame("Breakout Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel(width, height);
        frame.add(gamePanel, BorderLayout.CENTER);

        statusBar = new JLabel("Score: 0 | Lives: 3 | Level: 1");
        statusBar.setForeground(Color.WHITE);
        statusBar.setBackground(Color.BLACK);
        statusBar.setOpaque(true);
        statusBar.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.add(statusBar, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        newGameItem = new JMenuItem("New Game");
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        highScoresItem = new JMenuItem("High Scores");
        highScoresItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
        aboutItem = new JMenuItem("About");
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));

        gameMenu.add(newGameItem);
        gameMenu.add(highScoresItem);
        gameMenu.add(aboutItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);

        frame.setJMenuBar(menuBar);

        frame.pack(); // подгонка под размеры меню, панели и т д
        frame.setLocationRelativeTo(null); //центрирование на экране
    }

    public void show() {
        frame.setVisible(true);
    }


    public void updateStatusBar(int score, int lives, int level) {
        statusBar.setText("Score: " + score + " | Lives: " + lives + " | Level: " + level);
    }

    public void addMenuListener(JMenuItem item, ActionListener listener) {
        item.addActionListener(listener);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }

    public String showInputDialog(String message, String title) {
        return JOptionPane.showInputDialog(frame, message, title, JOptionPane.QUESTION_MESSAGE);
    }


    public void showGameOverMessage(int score) {
        gamePanel.setMessage("Game Over! Final Score: " + score, Color.RED);
        gamePanel.repaint();
    }

    public void showLevelCompleteMessage(int score) {
        gamePanel.setMessage("Level Complete! Score: " + score, Color.GREEN);
        gamePanel.repaint();
    }

    public void clearGameMessage() {
        gamePanel.setMessage(null, null);
        gamePanel.repaint();
    }


    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public JMenuItem getHighScoresItem() {
        return highScoresItem;
    }

    public JMenuItem getAboutItem() {
        return aboutItem;
    }

    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }
}