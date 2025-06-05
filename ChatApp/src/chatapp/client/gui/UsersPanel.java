package chatapp.client.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//JList отвечает только за отображение данных
//
//ListModel хранит сами данные, gри его изменении список автоматически перерисовывается


public class UsersPanel extends JPanel {
    private final JList<String> userList;
    private final DefaultListModel<String> userListModel;

    public UsersPanel() {
        setLayout(new BorderLayout());
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        add(new JScrollPane(userList), BorderLayout.CENTER);
    }

    public void updateUserList(List<String> users) {
        userListModel.clear();
        for (String user : users) {
            userListModel.addElement(user);
        }
    }
}