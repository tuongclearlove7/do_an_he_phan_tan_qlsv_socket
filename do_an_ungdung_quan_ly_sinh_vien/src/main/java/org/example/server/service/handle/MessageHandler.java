package org.example.server.service.handle;

import org.example.server.config.DatabaseConfig;
import org.example.server.GUI.StudentGUI;
import org.example.server.DAL.class_message;
import org.example.server.service.MessageService;
import org.example.server.service.SocketServerService;
import org.example.server.DAL.student_message;
import org.example.server.service.UtilService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class MessageHandler implements MessageService {

    //Business logic layer
    private final Socket socket1;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final DatabaseConfig dbConfig;
    private final class_message class_message;
    private final student_message student_message;
    private UtilService utilService;
    private final MessageService messageService;

    public MessageHandler() {
        dbConfig = new DatabaseConfig();
        class_message = new class_message(this);
        student_message = new student_message(this);
        socket1 = new Socket();
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
            @Override
            public void initialMessage(String message) {
                MessageService.super.initialMessage(message);
            }
        };
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
                String SQL2 = "SELECT * FROM classes WHERE classes.id = ?";
                this.handleMessage(message);
                student_message.handleClientMessageSearch(conn, message);
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
    public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {

    }

    @Override
    public void handleMessage(String message) {

        String[] options = {
            "Tìm kiếm theo mã sinh viên nhập: SV001",
            "Tìm kiếm tên sinh viên nhập: Nguyen",
            "Hiển thị danh sách sinh viên nhập: all",
            "Xóa tin nhắn nhập: clear"
        };
        StringBuilder str = new StringBuilder();
        str.append("Xin chào bạn muốn gì?")
        .append("\nNhập vào ô chat để tìm kiếm thông tin sinh viên!\n");
        for(int i = 0; i < options.length; i++){
            str.append("Option[")
            .append((i + 1))
            .append("]: ")
            .append(options[i])
            .append("\n");
        }
        if(message.isEmpty()){
            sendMessageToClient(String.valueOf(str));
        }else {
            messageService.initialMessage("Client: " + message + "\n");
        }
    }

    @Override
    public void sendMessageToClient(String response){
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
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException {}
}
