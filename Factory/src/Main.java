import factory.ConfigReader;
import factory.Factory;
import gui.FactoryGUI;

import javax.swing.*;
import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        try {
            ConfigReader config = new ConfigReader("config.properties");
            Factory factory = new Factory(config);
            SwingUtilities.invokeLater(() -> {
            FactoryGUI gui = new FactoryGUI(factory);
            factory.setGui(gui);
            gui.start();});

        } catch (IOException e) {
            System.err.println("Failed to start factory: " + e.getMessage());
        }
    }

}
