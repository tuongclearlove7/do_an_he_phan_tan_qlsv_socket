package org.example.server;

import org.example.database_config;
import org.example.server.classes.class_message;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class socket_server implements interface_socket_server{

    private static Socket socket1;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private final database_config dbConfig;
    private final interface_socket_server interfaceSocketServer;
    private class_message class_message;


    public socket_server(){

        dbConfig = new database_config();
//        class_message = new class_message();
        this.interfaceSocketServer = this;
    }

    public void connect_socket_server() {
        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            while (true) {
                socket1 = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
                System.err.println("Server Socket connected...");
                StudentApp.messageField.append("Client connected server chat..." + "\n");
                new Thread(this::listenForMessages).start();
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    @Override
    public void listenForMessages() {
        Connection conn = dbConfig.connect_database();
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.err.println("The message received from client: " + message);
                String SQL = "SELECT * FROM students WHERE students.code = '" + message + "';";
                handleMessage(message);
                this.handleStudentMessage(SQL, conn);

            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
        } catch (SQLException e) {
            System.err.println("ERROR: " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket1 != null) socket1.close();
            } catch (IOException e) {
                System.err.println("ERROR: " + e);
            }
        }
    }

    @Override
    public void handleMessage(String message){
        if(message.isEmpty()){
            sendMessageToClient("Xin chào bạn muốn gì? " +
                    "\nOption 1: Nhập mã sinh viên của bạn vào chat để xem thông tin của bạn vd: SV001");
        }else {
            SwingUtilities.invokeLater(() -> {
                StudentApp.messageField.append("Client: " + message + "\n");
            });
        }
    }

    @Override
    public void sendMessageToClient(String response) {
        try {
            if (writer != null) {
                writer.write(response);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e);
        }
    }

    @Override
    public void handleStudentMessage(String SQL, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(SQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder str = new StringBuilder();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String fullname = resultSet.getString("fullname");
            String birthday = resultSet.getString("birthday");
            String code = resultSet.getString("code");
            str.append("Thông tin của sinh viên ")
            .append(fullname)
            .append("\n")
            .append("ID: ")
            .append(id).append(", Fullname: ")
            .append(fullname).append(", Birthday: ")
            .append(birthday).append(", Code: ").append(code);
        }
        sendMessageToClient(String.valueOf(str));
    }
}
