package chatapp.messages;

import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;

public class LoginCommand extends XmlMessage implements Serializable, ChatMessage {
    private final String nickname;

    public LoginCommand(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root = doc.createElement("command");
        root.setAttribute("name", "login");
        doc.appendChild(root);
        root.appendChild(XmlHelper.createTextElement(doc, "name", nickname));
        return doc;
    }
}

