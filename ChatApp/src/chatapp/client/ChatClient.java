package chatapp.client;

import chatapp.Constants;
import chatapp.client.gui.ChatFrame;
import chatapp.messages.*;
import chatapp.switcher.Switch;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import javax.swing.*;

public class ChatClient {
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private final ChatFrame gui;
    private String sessionId;

    private String nickname;
    private boolean isConnected;

    public ChatClient(ChatFrame gui) {
        this.gui = gui;
        this.isConnected = false;
        this.sessionId = null;
    }

    public boolean connect(String host, int port, String nickname) {
        this.nickname = nickname;
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(Constants.TIMEOUT_MS);
            out = socket.getOutputStream();
            in = socket.getInputStream();

            Switch.sendMessage(new LoginCommand(nickname), out);

            ChatMessage response = Switch.receiveMessage(in);
            if (response instanceof SuccessMessage) {
                isConnected = true;
                new Thread(this::listenForMessages).start();
                return true;
            } else if (response instanceof ErrorMessage) {
                JOptionPane.showMessageDialog(gui, ((ErrorMessage) response).getReason());
                disconnect();
                return false;
            } else {
                JOptionPane.showMessageDialog(gui, "Unknown error");
                disconnect();
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, "Connection failed: " + e.getMessage());
            return false;
        }
    }

    public void sendTextMessage(String text) {
        if (!isConnected || socket.isClosed()) {
            gui.displayMessage("Cannot send message: Not connected to server");
            return;
        }
        try {
            Switch.sendMessage(new TextMessage(nickname, text, sessionId, LocalDateTime.now().toString()), out);
        } catch (IOException e) {
            gui.displayMessage("Failed to send message: " + e.getMessage());
        }
    }

    public void logout() {
        if (!isConnected || socket.isClosed()) {
            disconnect();
            return;
        }
        try {
            Switch.sendMessage(new LogoutCommand(sessionId), out);
        } catch (IOException e) {
            gui.displayMessage("Failed to logout: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void listenForMessages() {
        try {
            while (isConnected) {
                ChatMessage message = Switch.receiveMessage(in);
                SwingUtilities.invokeLater(() -> {
                    if (message instanceof TextMessage) {
                        gui.displayMessage(message.toString());
                    } else if (message instanceof UserListMessage) {
                        gui.updateUserList(((UserListMessage) message).getUsers());
                    } else if (message instanceof UserStatusMessage status) {
                        gui.displayMessage(status.getNickname() + (status.isConnected() ? " joined the chat" : " left the chat"));
                    } else if (message instanceof SuccessMessage) {
                        if(((SuccessMessage) message).getSession() != null && sessionId == null){
                            sessionId = ((SuccessMessage) message).getSession();
                        }
                    } else if (message instanceof ErrorMessage) {
                        gui.displayMessage("Error message: " + ((ErrorMessage) message).getReason());
                    } else {
                        gui.displayMessage("Unknown message received");
                    }
                });
            }
        } catch (SocketTimeoutException e) {
            SwingUtilities.invokeLater(() -> {
                gui.displayMessage("Connection timed out");
                gui.disableInput();
                isConnected = false;
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                gui.displayMessage("Connection lost: " + e.getMessage());
                gui.disableInput();
                isConnected = false;
            });
        } finally {
            disconnect();
        }
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while disconnecting: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}