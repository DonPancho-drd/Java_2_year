package chatapp.server;

import chatapp.Constants;
import chatapp.messages.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    private final ServerSocket serverSocket;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final List<TextMessage> messageHistory = new ArrayList<>();

    public ChatServer(int port) throws IOException {
        ServerLogger.init();
        serverSocket = new ServerSocket(port);
        ServerLogger.log("Server started on port " + port);
    }

    public void start() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(Constants.TIMEOUT_MS);
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            ServerLogger.log("Server error: " + e.getMessage());
        } finally {
            ServerLogger.close();
        }
    }

    public void broadcast(ChatMessage message) {
        if (message instanceof TextMessage) {
            synchronized (messageHistory) {
                messageHistory.add((TextMessage) message);
                if (messageHistory.size() > Constants.MESSAGE_HISTORY_LIMIT) {
                    messageHistory.remove(0);
                }
            }
        }
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
        if (message instanceof TextMessage) {
            ServerLogger.log("Broadcast message: " + message);
        }
    }
    public void removeRejectedClient(ClientHandler client) {
        clients.remove(client);
        ServerLogger.log("Client was not accepted");
    }

    public void removeClient(ClientHandler client, String nickname) {
        clients.remove(client);
        broadcast(new UserStatusMessage(nickname, false));
        broadcast(new UserListMessage(getUserList()));
        ServerLogger.log("Client disconnected: " + nickname);
    }

    public List<String> getUserList() {
        List<String> userList = new ArrayList<>();
        for (ClientHandler client : clients) {
                userList.add(client.getNickname());

        }
        return userList;
    }

    public List<TextMessage> getMessageHistory() {
        synchronized (messageHistory) {
            return new ArrayList<>(messageHistory);
        }
    }

    public boolean isNicknameTaken(String nickname) {
        for (ClientHandler client : clients) {
            if (nickname.equals(client.getNickname())) {
                return true;
            }
        }
        return false;
    }
}