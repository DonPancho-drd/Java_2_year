package chatapp.client;

import javax.swing.*;
import chatapp.client.gui.ChatFrame;
public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatFrame::new);
    }
}