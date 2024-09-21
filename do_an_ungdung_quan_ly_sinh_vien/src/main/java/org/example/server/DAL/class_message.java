package org.example.server.DAL;

import org.example.server.service.handle.MessageHandler;
import org.example.server.service.SocketServerService;
import org.example.server.socket.Socket;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class class_message implements SocketServerService {

    //Data access layer

    private MessageHandler messageHandler;
    private Socket Socket;


    public class_message(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public class_message(){

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
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException, IOException {
        if(conn!=null){
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            preparedStatement.setString(1, param);
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
            messageHandler.sendMessageToClient(String.valueOf(str));
        }
    }
}
