package chatapp.messages;

import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;

public class ErrorMessage extends XmlMessage implements Serializable, ChatMessage {
    private final String reason;

    public ErrorMessage(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root = doc.createElement("error");
        doc.appendChild(root);
        root.appendChild(XmlHelper.createTextElement(doc, "message", reason));
        return doc;
    }
}