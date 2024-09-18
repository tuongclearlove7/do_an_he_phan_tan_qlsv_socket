package org.example.server.service.handle;

import org.example.server.DAL.student_message;
import org.example.server.config.DatabaseConfig;
import org.example.server.service.StudentService;
import org.example.server.service.UtilService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class StudentHandler implements UtilService, StudentService {

    private final DatabaseConfig dbConfig;
    private final student_message student_message;


    public StudentHandler() {
        student_message = new student_message();
        dbConfig = new DatabaseConfig();
    }

    @Override
    public void reloads(DefaultTableModel tableModel) {
        student_message.reloads(tableModel);
    }

    @Override
    public void refesh(DefaultTableModel tableModel) {
        student_message.refesh(tableModel);
    }


    @Override
    public void create(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField
    ) {
        student_message.create(tableModel, idField,fullnameField, birthdayField);
    }

    @Override
    public StringBuilder findById(String SQL, Connection conn, String param) throws SQLException, IOException {

        String message = String.valueOf(student_message.findById(SQL, conn, param));
        if(message.isEmpty()){
            return new StringBuilder();
        }
        return student_message.findById(SQL, conn, param).append("\n");
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2, String param3) throws SQLException, IOException {
        if(student_message.search(SQL, conn, param1, param2, param3).isEmpty()){
            return new StringBuilder();
        }
        return student_message.search(SQL, conn, param1, param2, param3).append("\n");
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2) throws SQLException, IOException {
        return null;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1) throws SQLException, IOException {
        if(Objects.equals(param1, "all")){
            param1 = "";
            return student_message.search(SQL, conn, param1, null);
        }
        return student_message.search(SQL, conn, param1).append("\n");
    }

    @Override
    public void updateById(
            DefaultTableModel tableModel,
            JTextField idField,
            JTextField fullnameField,
            JTextField birthdayField,
            JTable studentTable
    ) {
        student_message.updateById(tableModel, idField, fullnameField, birthdayField, studentTable);
    }


    @Override
    @Transactional
    public void deleteById(
            DefaultTableModel tableModel,
            JTextField idField,
            JTable studentTable
    ) {
        student_message.deleteById(tableModel, idField, studentTable);
    }
}
