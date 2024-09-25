package org.example.server.service.event;

import org.example.server.GUI.ClassGUI;
import org.example.server.config.DatabaseConfig;
import org.example.server.service.MessageService;
import org.example.server.service.UtilService;
import org.example.server.service.handle.MessageHandler;
import org.example.server.socket.Socket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UtilEvent implements MessageService, UtilService {

    MessageService messageService;
    StudentEvent studentEvent;
    DatabaseConfig dbConfig;
    MessageHandler messageHandler;
    Socket socket;

    public UtilEvent() {
        messageService = new MessageService() {
            @Override
            public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {}
            @Override
            public void handleMessage(String message) {}
            @Override
            public void listenForMessages() {}
            @Override
            public void sendMessageToClient(String response) {}
            @Override
            public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException, IOException {}
        };
        socket = new Socket();
        studentEvent = new StudentEvent();
        dbConfig = new DatabaseConfig();
        messageHandler = new MessageHandler();
    }

    public void sendEvent(JTextField messageSendField, Socket socket){
        String message = messageSendField.getText();
        messageService.initialMessage("You: "+message + "\n");
        if (message != null && !message.isEmpty()) {
            socket.serverSendMessageToClient(message);
        }
    }

    public void clearEvent(JTextArea messageField, StringBuilder str){
        messageField.setText("");
        messageService.initialMessage(str.toString());
    }

    public void classGUIEvent(){
        new ClassGUI().setVisible(true);
    }

    @Override
    public void searchEvent(JTextField searchField){}

    @Override
    public void findAll(DefaultTableModel tableModel) {}

    @Override
    public void refresh(DefaultTableModel tableModel) {}

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
    public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {}

    @Override
    public void handleMessage(String message) {}

    @Override
    public void listenForMessages() {}

    @Override
    public void sendMessageToClient(String response) {}

    @Override
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException, IOException {}
}
