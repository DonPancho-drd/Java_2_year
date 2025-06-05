package chatapp.server;

import chatapp.Constants;

public class ServerMain {
    public static void main(String[] args) {
        int port = Constants.DEFAULT_PORT;
        try {
            ChatServer server = new ChatServer(port);
            server.start();
        } catch (Exception e) {
            ServerLogger.log("Failed to start server: " + e.getMessage());
        }
    }
}