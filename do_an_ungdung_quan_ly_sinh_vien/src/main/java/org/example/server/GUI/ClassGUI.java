package org.example.server.GUI;

import org.example.server.config.DatabaseConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClassGUI extends JFrame {

    //GUI (Graphic user interface)

    private JTable classTable;
    private DefaultTableModel tableModel;
    private final DatabaseConfig dbConfig;
    private JTextField idField;
    private JTextField classname;


    public ClassGUI() {
        dbConfig = new DatabaseConfig();

        setTitle("Quản lý lớp học");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        classTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(classTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Class name:"));
        classname = new JTextField();
        inputPanel.add(classname);
        add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Load");
        buttonPanel.add(loadButton);

        JButton addButton = new JButton("Add");
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update");
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("action");
            }
        });
    }

}
