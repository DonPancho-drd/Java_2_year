package chatapp.messages;

import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;

public class LogoutCommand extends XmlMessage implements Serializable, ChatMessage {
    private final String session;

    public LogoutCommand(String session) {
        this.session = session;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root = doc.createElement("command");
        root.setAttribute("name", "logout");
        doc.appendChild(root);
        root.appendChild(XmlHelper.createTextElement(doc, "session", session));
        return doc;
    }

    public String getSession() {
        return session;
    }
}