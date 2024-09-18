package org.example.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface interface_socket_server {

    void handleMessage(String message);

    void listenForMessages();

    void sendMessageToClient(String response);

    void handleStudentMessage(String SQL, Connection conn) throws SQLException;
}
