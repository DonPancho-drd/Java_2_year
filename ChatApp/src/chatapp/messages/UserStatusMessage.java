package chatapp.messages;

import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;

public class UserStatusMessage extends XmlMessage implements Serializable, ChatMessage {
    private final String nickname;
    private final boolean connected;

    public UserStatusMessage(String nickname, boolean connected) {
        this.nickname = nickname;
        this.connected = connected;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root = doc.createElement("event");
        root.setAttribute("name", connected ? "userlogin" : "userlogout");
        doc.appendChild(root);
        root.appendChild(XmlHelper.createTextElement(doc, "name", nickname));
        return doc;
    }
}