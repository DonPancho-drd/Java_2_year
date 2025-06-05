package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameModel model;
    private String message;
    private Color messageColor;

    public GamePanel(int width, int height) {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    public void setMessage(String message, Color color) {
        this.message = message;
        this.messageColor = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (model == null) return;

        model.getBall().paint(g2d);
        model.getPaddle().paint(g2d);
        for (Brick brick : model.getBricks()) {
            brick.paint(g2d);
        }
        for (PowerUp powerUp : model.getPowerUps()) {
            powerUp.paint(g2d);
        }

        if (message != null && messageColor != null) {
            drawCenteredMessage(g2d, message, messageColor);
        }
    }

    private void drawCenteredMessage(Graphics2D g2d, String message, Color color) {
        Font font = new Font("Arial", Font.BOLD, 18);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int msgWidth = fm.stringWidth(message);
        g2d.setColor(color);
        g2d.drawString(message, (getWidth() - msgWidth) / 2, getHeight() / 2);
    }
}