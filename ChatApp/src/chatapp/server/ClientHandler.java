package chatapp.server;

import chatapp.messages.*;
import chatapp.switcher.Switch;

import java.io.*;
import java.net.*;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer server;
    private String nickname;
    private String sessionId;
    private OutputStream out;
    private InputStream in;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        this.sessionId = null;
    }

    public String getNickname() {
        return nickname;
    }

    public void sendMessage(ChatMessage message) {
        try {
            Switch.sendMessage(message, out);
        } catch (IOException e) {
            ServerLogger.log("Failed to send message to " + nickname + ": " + e.getMessage());
        }
    }
    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    private boolean loginClient() throws IOException, ClassNotFoundException {
        ChatMessage message = Switch.receiveMessage(in);

        if (!(message instanceof LoginCommand)) {
            Switch.sendMessage(new ErrorMessage("LoginCommand was not received."), out);
            socket.close();
            return false;
        }

        String requestedNickname = ((LoginCommand) message).getNickname();
        if (server.isNicknameTaken(requestedNickname)) {
            Switch.sendMessage(new ErrorMessage("Nickname taken"), out);
            socket.close();
            return false;
        }

        nickname = requestedNickname;
        sessionId = generateSessionId();


        sendMessage(new SuccessMessage(sessionId));

        for (TextMessage msg : server.getMessageHistory()) {
            sendMessage(msg);
        }

        server.broadcast(new UserStatusMessage(nickname, true));
        server.broadcast(new UserListMessage(server.getUserList()));
        ServerLogger.log("Client logged in: " + nickname);
        return true;
    }

    @Override
    public void run() {
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();

            if (!loginClient()) {
                server.removeRejectedClient(this);
                return;
            }

            while (true) {
                try {
                    ChatMessage message = Switch.receiveMessage(in);
                    if (message instanceof TextMessage) {
                        server.broadcast(message);
                        sendMessage(new SuccessMessage());
                    } else if (message instanceof LogoutCommand) {
                        server.removeClient(this, nickname);
                        break;
                    } else {
                        sendMessage(new ErrorMessage("Unknown message received"));
                        ServerLogger.log("Unknown message received from client: " + nickname);
                    }
                } catch (SocketTimeoutException e) {
                    ServerLogger.log("Client " + nickname + " timed out");
                    server.removeClient(this, nickname);
                    break;
                } catch (IOException e) {
                    ServerLogger.log("IO error for " + nickname + ": " + e.getMessage());
                    server.removeClient(this, nickname);
                    break;
                } catch (ClassNotFoundException e) {
                    ServerLogger.log("Serialization error for " + nickname + ": " + e.getMessage());
                    server.removeClient(this, nickname);
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            ServerLogger.log("Client error (" + nickname + "): " + e.getMessage());
            server.removeClient(this, nickname);
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                socket.close();
            } catch (IOException e) {
                ServerLogger.log("Failed to close socket for " + nickname + ": " + e.getMessage());
            }
        }
    }
}