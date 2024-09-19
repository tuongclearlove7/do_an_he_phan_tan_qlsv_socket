package org.example.client.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientStudentGUI extends JFrame {

    //GUI (Graphic user interface)

    private final JTextField messageField;
    private final JTextArea chatArea;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private final String hostName = "localhost";
    private final int port = 2000;

    public ChatClientStudentGUI() {

        setTitle("Chat Client");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        connectToServer();

        sendButton.addActionListener(e -> sendMessage());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (socket != null && !socket.isClosed()) {
                        writer.println("Client disconnected server chat!");
                        socket.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void connectToServer() {
        new Thread(() -> {
            while (true) {
                try {
                    socket = new Socket(hostName, port);
                    OutputStream outputStream = socket.getOutputStream();
                    writer = new PrintWriter(outputStream, true);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer.println();
                    chatArea.append("Connected to server!\n");
                    new Thread(new ReceiveMessagesTask()).start();
                    break;
                } catch (Exception e) {
                    chatArea.append("Error connecting to server: " + e.getMessage() + "\n");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            writer.println(message);
            if(message.equals("clear")){
                writer.println();
                chatArea.setText("");
                new Thread(new ReceiveMessagesTask()).start();
            }
            chatArea.append("\nYou: " + message + "\n");
        }
    }

    private class ReceiveMessagesTask implements Runnable {
        @Override
        public void run() {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    System.out.println(response);
                    if(!response.isEmpty()){
                        chatArea.append("Server: " + response + "\n");
                    }
                }
            } catch (Exception e) {
                chatArea.append("Server not active: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatClientStudentGUI().setVisible(true));
    }
}

