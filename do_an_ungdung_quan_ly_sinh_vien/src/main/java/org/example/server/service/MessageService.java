package org.example.server.service;

import org.example.server.GUI.StudentGUI;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface MessageService {

    void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException;

    default void initialMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentGUI.messageField.append(message);
            }
        });
    }
}
