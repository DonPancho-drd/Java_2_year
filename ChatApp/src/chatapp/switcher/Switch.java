package chatapp.switcher;

import chatapp.Constants;
import chatapp.messages.*;
import chatapp.xmlversion.XmlMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Switch {
    public static void sendMessage(ChatMessage message, OutputStream out) throws IOException {
        try {
            switch (Constants.IsXmlVersion) {
                case true:
                    ((XmlMessage) message).sendXml(out);
                    break;
                case false:
                    ObjectOutputStream oos = new ObjectOutputStream(out);
                    oos.writeObject(message);
                    oos.flush();
                    break;
            }
        } catch (Exception e) {
            throw new IOException("Failed to send message", e);
        }
    }

    public static ChatMessage receiveMessage(InputStream in) throws IOException, ClassNotFoundException {
        try {
            switch (Constants.IsXmlVersion) {
                case true:
                    Document doc = XmlMessage.readXml(in);
                    return parseXmlMessage(doc);
                case false:
                    ObjectInputStream ois = new ObjectInputStream(in);
                    return (ChatMessage) ois.readObject();
            }
        } catch (Exception e) {
            throw new IOException("Failed to receive message", e);
        }
    }

    private static ChatMessage parseXmlMessage(Document doc) throws Exception {
        String rootTag = doc.getDocumentElement().getTagName();
        String session;
        switch (rootTag) {
            case "command":
                String name = doc.getDocumentElement().getAttribute("name");
                switch (name) {
                    case "login":
                        String nickname = doc.getElementsByTagName("name").item(0).getTextContent();
                        return new LoginCommand(nickname);

                    case "message":
                        String sender = doc.getElementsByTagName("name").item(0).getTextContent();
                        String content = doc.getElementsByTagName("message").item(0).getTextContent();
                        session = doc.getElementsByTagName("session").item(0) != null ?
                                doc.getElementsByTagName("session").item(0).getTextContent() : null;
                        String timestamp = doc.getElementsByTagName("time").item(0).getTextContent();
                        return new TextMessage(sender, content, session, timestamp);

                    case "logout":
                        session = doc.getElementsByTagName("session").item(0) != null ?
                                doc.getElementsByTagName("session").item(0).getTextContent() : null;
                        return new LogoutCommand(session);
                    default:
                        throw new UnsupportedOperationException("Unsupported command type: " + name);
                }
            case "event":
                String eventName = doc.getDocumentElement().getAttribute("name");
                switch (eventName) {
                    case "message":
                        String sender = doc.getElementsByTagName("name").item(0).getTextContent();
                        String content = doc.getElementsByTagName("message").item(0).getTextContent();
                        String timestamp = doc.getElementsByTagName("time").item(0).getTextContent();
                        return new TextMessage(sender, content, timestamp);

                    case "userlogin":
                        String nickname = doc.getElementsByTagName("name").item(0).getTextContent();
                        return new UserStatusMessage(nickname, true);
                    case "userlogout":
                        nickname = doc.getElementsByTagName("name").item(0).getTextContent();
                        return new UserStatusMessage(nickname, false);
                    default:
                        throw new UnsupportedOperationException("Unsupported event type: " + eventName);
                }
            case "success":
                NodeList listUsers = doc.getElementsByTagName("listusers");
                if (listUsers.getLength() > 0) {
                    List<String> users = new ArrayList<>();
                    NodeList userNodes = doc.getElementsByTagName("user");
                    for (int i = 0; i < userNodes.getLength(); i++) {
                        Element userElement = (Element) userNodes.item(i);
                        String nickname = userElement.getElementsByTagName("name").item(0).getTextContent();
                        users.add(nickname);
                    }
                    return new UserListMessage(users);
                }
                session = doc.getElementsByTagName("session").getLength() > 0 ?
                        doc.getElementsByTagName("session").item(0).getTextContent() : null;
                return new SuccessMessage(session);
            case "error":
                String reason = doc.getElementsByTagName("message").item(0).getTextContent();
                return new ErrorMessage(reason);
            default:
                throw new UnsupportedOperationException("Unsupported XML message type: " + rootTag);
        }
    }
}