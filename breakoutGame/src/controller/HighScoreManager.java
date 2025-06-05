package controller;

import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import view.GameView;
import javax.swing.*;

public class HighScoreManager {
    private final List<ScoreEntry> highScores = new ArrayList<>();
    private final String fileName;
    private final GameView view;

    public HighScoreManager(String fileName, GameView view) {
        this.fileName = fileName;
        this.view = view;
        loadHighScoresFromFile();
    }

    private void loadHighScoresFromFile() {
        highScores.clear();
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String playerName = parts[0];
                    int playerScore = Integer.parseInt(parts[1]);
                    highScores.add(new ScoreEntry(playerName, playerScore));
                }
            }
            highScores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        } catch (IOException | NumberFormatException ex) {
            view.showMessage("Ошибка при загрузке очков: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveHighScore(String name, int score) {
        highScores.add(new ScoreEntry(name, score));
        highScores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        saveHighScoresToFile();
    }

    private void saveHighScoresToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (ScoreEntry entry : highScores) {
                writer.println(entry.getName() + ":" + entry.getScore());
            }
        } catch (IOException ex) {
            view.showMessage("Ошибка при сохранении рекордов: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showHighScores(ActionEvent e) {
        StringBuilder message = new StringBuilder("High Scores:\n");
        if (highScores.isEmpty()) {
            message.append("No scores yet!");
        } else {
            for (ScoreEntry entry : highScores) {
                message.append(entry.getName()).append(": ").append(entry.getScore()).append("\n");
            }
        }
        view.showMessage(message.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }



    private static class ScoreEntry { //nested
        private final String name;
        private final int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}