package org.example.server.classes;

import org.example.server.interface_socket_server;
import org.example.server.socket_server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class class_message implements interface_socket_server {


    private socket_server socket_server;

    public class_message() {
        socket_server = new socket_server();
    }

    @Override
    public void handleMessage(String message) {

    }

    @Override
    public void listenForMessages() {

    }

    @Override
    public void sendMessageToClient(String response) {

    }

    @Override
    public void handleStudentMessage(String SQL, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder str = new StringBuilder();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            str.append("Thông tin của lớp ")
            .append(name)
            .append("\n")
            .append("ID: ")
            .append(id).append(", Class name: ")
            .append(name);
        }
        socket_server.sendMessageToClient(String.valueOf(str));
    }
}
