package controller;

import model.Brick;
import model.GameModel;
import view.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class InputHandler {
    private final GameModel model;
    private final JComponent component;
    private final GameView view;

    public InputHandler(GameModel model, JComponent component, GameView view) {
        this.model = model;
        this.component = component;
        this.view = view;

        setupKeyBindings();
    }

    private void setupKeyBindings() {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getPaddle().setDefaultDx(-1);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getPaddle().setDefaultDx(1);
            }
        });


        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "stop");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "stop");
        actionMap.put("stop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getPaddle().setDx(0);
            }
        });


        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "space");
        actionMap.put("space", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getBall().isStuck()) {
                    view.clearGameMessage();
                    model.getBall().setStuck(false);
                    model.setBricksStuck(false);

                }
            }
        });


    }
}