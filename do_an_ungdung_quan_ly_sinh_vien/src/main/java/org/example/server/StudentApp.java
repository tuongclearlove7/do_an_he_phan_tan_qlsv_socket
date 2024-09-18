package org.example.server;

import org.example.database_config;
import org.example.server.classes.class_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;

public class StudentApp extends JFrame {
    private final JTextField idField;
    private final JTextField fullname;
    private final JTextField birthdayField;
    public static JTextField messageSendField;
    private final JTable studentTable;
    private final DefaultTableModel tableModel;
    public static JTextArea messageField;

    private final database_config dbConfig;
    private final socket_server socket_server;


    public StudentApp() throws IOException {

        dbConfig = new database_config();
        socket_server = new socket_server();
        startSocketServer();

        setTitle("Quản lý sinh viên");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        add(tableScrollPane, BorderLayout.CENTER);

        loadStudents();

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Full Name:"));
        fullname = new JTextField();
        inputPanel.add(fullname);

        inputPanel.add(new JLabel("Birthday:"));
        birthdayField = new JTextField();
        inputPanel.add(birthdayField);

        inputPanel.add(new JLabel("Message send:"));
        messageSendField = new JTextField();
        inputPanel.add(messageSendField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(new JLabel("Message received:"), BorderLayout.NORTH);

        messageField = new JTextArea(10, 30);
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

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(classButton);
        buttonPanel.add(sendButton);
        add(buttonPanel, BorderLayout.EAST);

        classButton.addActionListener(e -> {
            class_gui classWindow = new class_gui();
            classWindow.setVisible(true);
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageSendField.getText();
                messageField.append("You: "+message + "\n");
                if (message != null && !message.isEmpty()) {
                    socket_server.sendMessageToClient(message);
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refesh();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
                refesh();
                clearFields();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
                refesh();
                clearFields();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
                refesh();
                clearFields();
            }
        });
    }

    private void startSocketServer() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socket_server.connect_socket_server();
            }
        }).start();
    }


    private void refesh() {
        tableModel.setRowCount(0);
        loadStudents();
    }

    private void loadStudents() {
        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM students";
                Statement execute = conn.createStatement();
                ResultSet rs = execute.executeQuery(sql);

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = metaData.getColumnName(i);
                }
                tableModel.setColumnIdentifiers(columnNames);

                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    tableModel.addRow(row);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void addStudent() {
        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                String sql = "INSERT INTO students (id, fullname, birthday, code) VALUES (?, ?, ?, ?)";
                PreparedStatement execute = conn.prepareStatement(sql);
                execute.setString(1, idField.getText());
                execute.setString(2, fullname.getText().isEmpty() ? null : fullname.getText());
                execute.setString(3, birthdayField.getText().isEmpty() ? null : birthdayField.getText());
                execute.setString(4, idField.getText());
                execute.executeUpdate();

                tableModel.addRow(new Object[]{idField.getText(), fullname.getText(), birthdayField.getText()});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateStudent() {
        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow == -1) {
                    return;
                }
                String oldFullName = tableModel.getValueAt(selectedRow, 1).toString();
                String oldBirthday = tableModel.getValueAt(selectedRow, 2).toString();
                String newFullName = fullname.getText().isEmpty() ? oldFullName : fullname.getText();
                String newBirthday = birthdayField.getText().isEmpty() ? oldBirthday : birthdayField.getText();
                String sql = "UPDATE students SET fullname = ?, birthday = ? WHERE id = ?";
                PreparedStatement execute = conn.prepareStatement(sql);
                execute.setString(1, newFullName);
                execute.setString(2, newBirthday);
                execute.setString(3, idField.getText());
                execute.executeUpdate();

                tableModel.setValueAt(newFullName, selectedRow, 1);
                tableModel.setValueAt(newBirthday, selectedRow, 2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void deleteStudent() {
        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                String sql = "DELETE FROM students WHERE id = ?";
                PreparedStatement execute = conn.prepareStatement(sql);
                execute.setString(1, idField.getText());
                execute.executeUpdate();
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        idField.setText("");
        fullname.setText("");
        birthdayField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new StudentApp().setVisible(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

