package chatapp.client.gui;

import chatapp.client.ChatClient;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {
    private final JTextArea messageArea;
    private final JTextField inputField;
    private final JButton sendButton;

    public MessagePanel(ChatClient client) {
        setLayout(new BorderLayout());


        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                client.sendTextMessage(text);
                inputField.setText("");
            }
        });
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void displayMessage(String message) {
        messageArea.append(message + "\n");
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void disableInput() {
        inputField.setEnabled(false);
        sendButton.setEnabled(false);
    }
}