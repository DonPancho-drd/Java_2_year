package chatapp.messages;

import chatapp.xmlversion.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.Serializable;
import java.util.List;

public class UserListMessage extends XmlMessage implements Serializable, ChatMessage {
    private final List<String> users;

    public UserListMessage(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    @Override
    public Document toXml() throws Exception {
        Document doc = XmlHelper.newDocument();
        Element root = doc.createElement("success");
        doc.appendChild(root);
        Element listUsers = doc.createElement("listusers");
        root.appendChild(listUsers);
        for (String user : users) {
            Element userElement = doc.createElement("user");
            userElement.appendChild(XmlHelper.createTextElement(doc, "name", user));
            listUsers.appendChild(userElement);
        }
        return doc;
    }
}