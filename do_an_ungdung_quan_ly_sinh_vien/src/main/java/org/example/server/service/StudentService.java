package org.example.server.service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface StudentService {

    void createStudentEvent(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField,
            Runnable runnable
    );

    void updateStudentEvent(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField,
            JTable studentTable,
            Runnable runnable
    );

    void deleteStudentEvent(
            DefaultTableModel tableModel,
            JTextField idField,
            JTable studentTable,
            Runnable runnable
    );

    void createStudent(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField
    ) throws SQLException;

    void updateStudentById(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField,
            JTable studentTable
    ) throws SQLException;

    void deleteStudentById(
            DefaultTableModel tableModel,
            JTextField idField,
            JTable studentTable
    );
}
