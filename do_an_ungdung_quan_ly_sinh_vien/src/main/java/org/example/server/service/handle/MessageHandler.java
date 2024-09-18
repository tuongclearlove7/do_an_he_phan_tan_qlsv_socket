package org.example.server.service.handle;

import org.example.server.config.DatabaseConfig;
import org.example.server.GUI.StudentGUI;
import org.example.server.DAL.class_message;
import org.example.server.service.SocketServerService;
import org.example.server.DAL.student_message;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class MessageHandler implements SocketServerService {

    private final Socket socket1;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final DatabaseConfig dbConfig;
    private final class_message class_message;
    private final student_message student_message;

    public MessageHandler() {
        dbConfig = new DatabaseConfig();
        class_message = new class_message(this);
        student_message = new student_message(this);
        socket1 = new Socket();
    }

    public void settings(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void listenForMessages() {
        Connection conn = dbConfig.connect_database();
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.err.println("The message received from client: " + message);
                String SQL = "SELECT * FROM students WHERE students.code = ?";
                String SQL2 = "SELECT * FROM classes WHERE classes.id = ?";
                this.handleMessage(message);
                student_message.handleClientMessage(SQL, conn, message);
                class_message.handleClientMessage(SQL2, conn, message);
            }
        } catch (IOException e) {
            System.err.println("ERROR1: " + e);
        } catch (SQLException e) {
            System.err.println("ERROR2: " + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket1 != null) socket1.close();
            } catch (IOException e) {
                System.err.println("ERROR3: " + e);
            }
        }
    }

    @Override
    public void handleMessage(String message) {
        StringBuilder str = new StringBuilder();
        str.append("Xin chào bạn muốn gì? ")
        .append("\nOption 1: Nhập mã sinh viên ")
        .append("của bạn vào chat để xem thông tin của bạn example: SV001 ")
        .append("\nOption 2: Nhập mã lớp của bạn để ")
        .append("xem thông tin lớp example: C001")
        .append("");
        if(message.isEmpty()){
            sendMessageToClient(String.valueOf(str));
        }else {
            SwingUtilities.invokeLater(() -> {
                StudentGUI.messageField.append("Client: " + message + "\n");
            });
        }
    }

    @Override
    public void sendMessageToClient(String response){
        System.err.println("server send message..." + response);
        System.err.println(writer);

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
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException {

    }
}
