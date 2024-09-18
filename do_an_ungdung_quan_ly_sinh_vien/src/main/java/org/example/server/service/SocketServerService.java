package org.example.server.service;

import org.example.server.GUI.StudentGUI;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface SocketServerService {

    void handleMessage(String message);

    void listenForMessages();

    void sendMessageToClient(String response);

    void handleClientMessage(String SQL, Connection conn,String param) throws SQLException, IOException;
}
