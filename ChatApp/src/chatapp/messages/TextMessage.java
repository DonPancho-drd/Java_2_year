package chatapp.messages;

import chatapp.Constants;
import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;
import java.time.LocalDateTime;

public class TextMessage extends XmlMessage implements Serializable, ChatMessage {
    private final String sender;
    private final String content;
    private final String timestamp;
    private final String session;

    public TextMessage(String sender, String content, String now) {
        this(sender, content, null, now);
    }

    public TextMessage(String sender, String content, String session, String now) {
        this.sender = sender;
        this.content = content;
        this.session = session;
        this.timestamp = now;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender + ": " + content;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root;
        if (session != null) {  //от клиента
            root = doc.createElement("command");
            root.setAttribute("name", "message");
            root.appendChild(XmlHelper.createTextElement(doc, "name", sender));
            root.appendChild(XmlHelper.createTextElement(doc, "message", content));
            root.appendChild(XmlHelper.createTextElement(doc, "session", session));
            root.appendChild(XmlHelper.createTextElement(doc, "time", timestamp));
        } else {
            root = doc.createElement("event");
            root.setAttribute("name", "message");
            root.appendChild(XmlHelper.createTextElement(doc, "name", sender));
            root.appendChild(XmlHelper.createTextElement(doc, "message", content));
            root.appendChild(XmlHelper.createTextElement(doc, "time", timestamp));
        }
        doc.appendChild(root);
        return doc;
    }
}