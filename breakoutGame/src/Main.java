import controller.GameController;
import model.GameModel;
import view.GameView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel(600, 600);
            GameView view = new GameView(600, 600); // >= model width, height
            new GameController(model, view);
            view.show();
        });
    }
}