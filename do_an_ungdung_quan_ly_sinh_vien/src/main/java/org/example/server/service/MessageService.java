package org.example.server.service;

import org.example.server.GUI.StudentGUI;

import javax.swing.*;

public interface MessageService {

    default void initialMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentGUI.messageField.append(message);
            }
        });
    }
}
