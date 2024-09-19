package org.example.server.GUI;

import org.example.server.config.DatabaseConfig;
import org.example.server.service.MessageService;
import org.example.server.service.event.UtilEvent;
import org.example.server.service.handle.MessageHandler;
import org.example.server.service.handle.StudentHandler;
import org.example.server.socket.Socket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class StudentGUI extends JFrame {

    //GUI (Graphic user interface)
    private final JTextField idField;
    private final JTextField fullnameField;
    private final JTextField birthdayField;
    public static JTextField messageSendField;
    public static JTextField searchField;
    public static JTextArea messageField;
    private final JTable studentTable;
    private final DefaultTableModel tableModel;
    private final DatabaseConfig dbConfig;
    private final Socket Socket;
    private final MessageHandler messageHandler;
    private final StudentHandler studentHandler;
    private final MessageService messageService;
    private final UtilEvent utilEvent;

    public StudentGUI() throws IOException {

        StringBuilder str = new StringBuilder();
        str.append("Nhập vào ô search để tìm kiếm thông tin sinh viên theo ID, tên.")
        .append("\nOption[1]: Tìm kiếm theo mã sinh viên example: SV001")
        .append("\nOption[2]: Tìm kiếm tên sinh viên example: Nguyen")
        .append("\nOption[3]: Hiển thị danh sách sinh viên example: all")
        .append("\n");
        dbConfig = new DatabaseConfig();
        Socket = new Socket();
        messageHandler = new MessageHandler();
        studentHandler = new StudentHandler();
        utilEvent = new UtilEvent();
        messageService = new MessageService() {};
        messageService.initialMessage(str.toString());
        this.socketInitial();

        setTitle("Quản lý sinh viên");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        add(tableScrollPane, BorderLayout.CENTER);

        studentHandler.reloads(tableModel);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Full Name:"));
        fullnameField = new JTextField();
        inputPanel.add(fullnameField);

        inputPanel.add(new JLabel("Birthday:"));
        birthdayField = new JTextField();
        inputPanel.add(birthdayField);

        inputPanel.add(new JLabel("Search:"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        inputPanel.add(new JLabel("Message send:"));
        messageSendField = new JTextField();
        inputPanel.add(messageSendField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JLabel("Message received:"), BorderLayout.NORTH);

        messageField = new JTextArea(10, 20);
        messageField.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageField);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);

        add(messagePanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Load");
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton classButton = new JButton("Class");
        JButton sendButton = new JButton("Send");
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(classButton);
        buttonPanel.add(sendButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.EAST);

        classButton.addActionListener(e -> {utilEvent.classGUIEvent();});
        clearButton.addActionListener(e -> {utilEvent.clearEvent(messageField,str);});
        searchButton.addActionListener( e -> {utilEvent.searchEvent(searchField);});
        sendButton.addActionListener(e -> {utilEvent.sendEvent(messageSendField, Socket);});
        loadButton.addActionListener(e -> {utilEvent.reloads(tableModel);});
        addButton.addActionListener(e -> {studentHandler.createStudentEvent(
                tableModel, idField, fullnameField, birthdayField, this::clearFields
            );
        });
        updateButton.addActionListener(e -> {studentHandler.updateStudentEvent(
                tableModel, idField, fullnameField, birthdayField, studentTable, this::clearFields
            );
        });
        deleteButton.addActionListener(e -> {studentHandler.deleteStudentEvent(
                tableModel, idField, studentTable, this::clearFields
            );
        });
    }

    private void socketInitial() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket.connect_socket_server();
            }
        }).start();
    }

    private void clearFields() {
        idField.setText("");
        fullnameField.setText("");
        birthdayField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new StudentGUI().setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

