package org.example.server.socket;

import org.example.server.config.DatabaseConfig;
import org.example.server.GUI.StudentGUI;
import org.example.server.DAL.class_message;
import org.example.server.config.EnvConfig;
import org.example.server.service.MessageService;
import org.example.server.service.handle.MessageHandler;
import org.example.server.service.SocketServerService;
import org.example.server.DAL.student_message;

import java.io.*;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.SQLException;

public class Socket implements SocketServerService {

    private static java.net.Socket socket1;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private DatabaseConfig dbConfig;

    private SocketServerService interfaceSocketServer;

    private class_message class_message;
    private student_message student_message;
    private final MessageHandler messageHandler;
    private final EnvConfig envConfig;
    MessageService messageService;

    public Socket(){
        messageHandler = new MessageHandler();
        dbConfig = new DatabaseConfig();
        class_message = new class_message();
        student_message = new student_message();
        envConfig = new EnvConfig();
        messageService = new MessageService() {
            @Override
            public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {

            }
        };

    }
    public void connect_socket_server() {
        try {
            long port = Long.parseLong(envConfig.getPort());
            ServerSocket serverSocket = new ServerSocket((int) port);

            while (true) {
                socket1 = serverSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
                System.err.println("Server Socket connected...");
                messageService.initialMessage("\nClient connected server chat..." + "\n");
                messageHandler.settings(reader, writer);
                new Thread(messageHandler::listenForMessages).start();
            }
        } catch (Exception e) {
            System.err.println("Error connect socket server: " + e);
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
    public void handleMessage(String message) {

    }

    @Override
    public void listenForMessages() {

    }

    @Override
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException, IOException {

    }
}
