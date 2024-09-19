package org.example.server.DAL;

import org.example.server.config.DatabaseConfig;
import org.example.server.service.StudentService;
import org.example.server.service.UtilService;
import org.example.server.service.handle.MessageHandler;
import org.example.server.service.SocketServerService;
import org.example.server.socket.Socket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class student_message implements SocketServerService, StudentService, UtilService {

    //Data access layer

    private Socket Socket;
    private MessageHandler messageHandler;
    private DatabaseConfig dbConfig;


    public student_message(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public student_message(){
        dbConfig = new DatabaseConfig();
    }


    @Override
    public void handleMessage(String message) {

    }

    @Override
    public void listenForMessages() {

    }

    @Override
    public void sendMessageToClient(String response) {

    }

    @Override
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException, IOException {
        PreparedStatement preparedStatement = conn.prepareStatement(SQL);
        preparedStatement.setString(1, param);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder str = new StringBuilder();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String fullname = resultSet.getString("fullname");
            String birthday = resultSet.getString("birthday");
            String code = resultSet.getString("code");
            str.append("Thông tin của sinh viên ")
            .append(fullname)
            .append("\n")
            .append("ID: ")
            .append(id).append(", Fullname: ")
            .append(fullname).append(", Birthday: ")
            .append(birthday).append(", Code: ").append(code);
        }
        messageHandler.sendMessageToClient(String.valueOf(str));
    }

    @Override
    public void createStudent(DefaultTableModel tableModel, JTextField idField, JTextField fullnameField, JTextField birthdayField) throws SQLException {
        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                String sql = "INSERT INTO students (id, fullname, birthday, code) VALUES (?, ?, ?, ?)";
                PreparedStatement execute = conn.prepareStatement(sql);
                execute.setString(1, idField.getText());
                execute.setString(2, fullnameField.getText().isEmpty() ? null : fullnameField.getText());
                execute.setString(3, birthdayField.getText().isEmpty() ? null : birthdayField.getText());
                execute.setString(4, idField.getText());
                execute.executeUpdate();

                tableModel.addRow(new Object[]{idField.getText(), fullnameField.getText(), birthdayField.getText()});
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    @Override
    public void createStudentEvent(DefaultTableModel tableModel, JTextField idField, JTextField fullnameField, JTextField birthdayField, Runnable runnable) {

    }

    @Override
    public void updateStudentEvent(DefaultTableModel tableModel, JTextField idField, JTextField fullnameField, JTextField birthdayField, JTable studentTable, Runnable runnable) {

    }

    @Override
    public void deleteStudentEvent(DefaultTableModel tableModel, JTextField idField, JTable studentTable, Runnable runnable) {

    }

    @Override
    public void updateStudentById(DefaultTableModel tableModel, JTextField idField, JTextField fullnameField, JTextField birthdayField, JTable studentTable) throws SQLException {

        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                int selectedRow = studentTable.getSelectedRow();
                String oldFullName = tableModel.getValueAt(selectedRow, 1).toString();
                String oldBirthday = tableModel.getValueAt(selectedRow, 2).toString();
                if (selectedRow == -1) {
                    return;
                }
                String newFullName = fullnameField.getText().isEmpty() ? oldFullName : fullnameField.getText();
                String newBirthday = birthdayField.getText().isEmpty() ? oldBirthday : birthdayField.getText();

                String sql = "UPDATE students SET fullname = ?, birthday = ? WHERE id = ?";

                PreparedStatement execute = conn.prepareStatement(sql);
                execute.setString(1, newFullName);
                execute.setString(2, newBirthday);
                execute.setString(3, idField.getText());
                execute.executeUpdate();

                tableModel.setValueAt(newFullName, selectedRow, 1);
                tableModel.setValueAt(newBirthday, selectedRow, 2);
                tableModel.setValueAt(idField.getText(), selectedRow, 3);

            } catch (SQLException e) {
                throw e;
            }
        }
    }

    @Override
    public void deleteStudentById(DefaultTableModel tableModel, JTextField idField, JTable studentTable) {
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

    @Override
    public void reloads(DefaultTableModel tableModel) {

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

    @Override
    public void refesh(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        reloads(tableModel);
    }

    @Override
    public StringBuilder findById(String SQL, Connection conn, String param) throws SQLException, IOException {
        SQL = "SELECT * FROM students WHERE students.code = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(SQL);
        preparedStatement.setString(1, param);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder str = new StringBuilder();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String fullname = resultSet.getString("fullname");
            String birthday = resultSet.getString("birthday");
            String code = resultSet.getString("code");
            str.append("\nKết quả tìm kếm thông tin theo ID của sinh viên ")
            .append(fullname)
            .append("\n")
            .append("ID: ")
            .append(id).append(", Fullname: ")
            .append(fullname).append(", Birthday: ")
            .append(birthday).append(", Code: ")
            .append(code)
            .append("\n");
        }
        return str;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2, String param3) throws SQLException, IOException {
        SQL = "SELECT * FROM students WHERE students.code = ?";
        StringBuilder query = new StringBuilder("SELECT * FROM students WHERE 1=1");
        query.append(" OR students.code = ?");
        query.append(" OR students.fullname LIKE ?");
        query.append(" OR students.birthday LIKE ?");

        PreparedStatement execute = conn.prepareStatement(query.toString());
        int index = 1;
        execute.setString(index++, "%" + param1 + "%");
        execute.setString(index++, "%" + param2 + "%");
        execute.setString(index++, "%" + param3 + "%");
        ResultSet resultSet = execute.executeQuery();
        StringBuilder str = new StringBuilder();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String fullname = resultSet.getString("fullname");
            String birthday = resultSet.getString("birthday");
            String code = resultSet.getString("code");
            str.append("\nKết quả tìm kếm thông tin của sinh viên ")
            .append(fullname)
            .append("\n")
            .append("ID: ")
            .append(id).append(", Fullname: ")
            .append(fullname).append(", Birthday: ")
            .append(birthday).append(", Code: ")
            .append(code)
            .append("\n");
        }
        return str;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2) throws SQLException, IOException {
        StringBuilder query = new StringBuilder("SELECT * FROM students WHERE");
        query.append(" students.fullname LIKE ?");
        PreparedStatement execute = conn.prepareStatement(query.toString());
        int index = 1;
        execute.setString(index++, "%" + param1 + "%");
        ResultSet resultSet = execute.executeQuery();
        StringBuilder str = new StringBuilder();
        str.append("\nKết quả tìm kếm danh sách sinh viên:");
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String fullname = resultSet.getString("fullname");
            String birthday = resultSet.getString("birthday");
            String code = resultSet.getString("code");
            str.append("\n")
            .append("ID: ")
            .append(id).append(", Fullname: ")
            .append(fullname).append(", Birthday: ")
            .append(birthday).append(", Code: ").append(code);
        }
        return str;
    }

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1) throws SQLException, IOException {

        StringBuilder query = new StringBuilder("SELECT * FROM students WHERE");
        query.append(" students.fullname LIKE ?");
        PreparedStatement execute = conn.prepareStatement(query.toString());
        int index = 1;
        execute.setString(index++, "%" + param1 + "%");
        ResultSet resultSet = execute.executeQuery();
        StringBuilder str = new StringBuilder();

        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String fullname = resultSet.getString("fullname");
            String birthday = resultSet.getString("birthday");
            String code = resultSet.getString("code");
            str.append("\nKết quả tìm kếm thông tin theo tên của sinh viên ")
            .append(fullname)
            .append("\n")
            .append("ID: ")
            .append(id).append(", Fullname: ")
            .append(fullname).append(", Birthday: ")
            .append(birthday).append(", Code: ").append(code)
            .append("\n");
        }
        return str;
    }

}
