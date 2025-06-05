package chatapp.messages;

import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;

public class SuccessMessage extends XmlMessage implements Serializable, ChatMessage {
    private final String session;

    public SuccessMessage() {
        this(null);
    }

    public SuccessMessage(String session) {
        this.session = session;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root = doc.createElement("success");
        doc.appendChild(root);
        root.appendChild(XmlHelper.createTextElement(doc, "session", session));
        return doc;
    }

    public String getSession() {
        return session;
    }
}