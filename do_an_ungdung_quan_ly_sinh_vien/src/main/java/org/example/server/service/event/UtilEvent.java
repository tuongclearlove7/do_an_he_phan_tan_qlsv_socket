package org.example.server.service.event;

import org.example.server.GUI.ClassGUI;
import org.example.server.config.DatabaseConfig;
import org.example.server.service.MessageService;
import org.example.server.service.StudentService;
import org.example.server.service.UtilService;
import org.example.server.service.handle.StudentHandler;
import org.example.server.socket.Socket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UtilEvent implements MessageService, UtilService {

    MessageService messageService;
    StudentHandler studentHandler;
    DatabaseConfig dbConfig;

    public UtilEvent() {
        messageService = new MessageService() {
            @Override
            public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {

            }
        };
        studentHandler = new StudentHandler();
        dbConfig = new DatabaseConfig();
    }

    public void sendEvent(JTextField messageSendField, Socket socket){
        String message = messageSendField.getText();
        messageService.initialMessage("You: "+message + "\n");
        if (message != null && !message.isEmpty()) {
            socket.sendMessageToClient(message);
        }
    }

    public void clearEvent(JTextArea messageField, StringBuilder str){
        messageField.setText("");
        messageService.initialMessage(str.toString());
    }

    @Override
    public void searchEvent(JTextField searchField){


    }

    public void classGUIEvent(){
        ClassGUI classWindow = new ClassGUI();
        classWindow.setVisible(true);
    }

    @Override
    public void reloads(DefaultTableModel tableModel) {
    }

    @Override
    public void refresh(DefaultTableModel tableModel) {
    }

    @Override
    public StringBuilder findById(String SQL, Connection conn, String param) throws SQLException, IOException {
        return null;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2, String param3) throws SQLException, IOException {
        return null;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2) throws SQLException, IOException {
        return null;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1) throws SQLException, IOException {
        return null;
    }

    @Override
    public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {

    }
}
