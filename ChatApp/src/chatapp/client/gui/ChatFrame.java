package chatapp.client.gui;

import chatapp.Constants;
import chatapp.client.ChatClient;

import javax.swing.*;
import java.awt.*;

public class ChatFrame extends JFrame {
    private final MessagePanel messagePanel;
    private final UsersPanel usersPanel;
    private final ChatClient client;
    private final JButton sendButton;
    private final JButton logoutButton;

    public ChatFrame() {
        setTitle("Chat App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        String nickname = JOptionPane.showInputDialog(this, "Enter your nickname:");
        if (nickname == null || nickname.trim().isEmpty()) {
            System.exit(0);
        }

        client = new ChatClient(this);
        if (!client.connect(Constants.SERVER_HOST, Constants.DEFAULT_PORT, nickname.trim())) {
            System.exit(1);
        }

        messagePanel = new MessagePanel(client);
        sendButton = messagePanel.getSendButton();
        add(messagePanel, BorderLayout.CENTER);

        usersPanel = new UsersPanel();
        add(usersPanel, BorderLayout.EAST);


        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            client.logout();
            disableInput();
            dispose();
        });
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    public void displayMessage(String message) {
        messagePanel.displayMessage(message);
    }

    public void updateUserList(java.util.List<String> users) {
        usersPanel.updateUserList(users);
    }

    public void disableInput() {
        logoutButton.setEnabled(false);
        messagePanel.disableInput();
    }

    @Override
    public void dispose() {
        client.disconnect();
        super.dispose();
    }
}