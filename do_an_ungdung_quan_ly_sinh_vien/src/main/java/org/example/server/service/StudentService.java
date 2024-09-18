package org.example.server.service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface StudentService {
    void create(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField
    );

    void updateById(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField,
            JTable studentTable
    );

    void deleteById(
            DefaultTableModel tableModel,
            JTextField idField,
            JTable studentTable
    );
}
