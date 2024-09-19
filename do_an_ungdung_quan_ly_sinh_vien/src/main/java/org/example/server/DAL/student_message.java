package org.example.server.DAL;

import org.example.server.DAL.model.Student;
import org.example.server.DAL.repository.StudentRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class student_message implements SocketServerService, StudentService, UtilService {

    //Data access layer
    private MessageHandler messageHandler;
    private final DatabaseConfig dbConfig;
    private final StudentRepository studentRepository;


    public student_message(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        this.dbConfig = new DatabaseConfig();
        this.studentRepository = new StudentRepository();
    }

    public student_message(){
        dbConfig = new DatabaseConfig();
        studentRepository = new StudentRepository();
    }

    @Override
    public void handleClientMessage(String SQL, Connection conn, String param) throws SQLException, IOException {
        PreparedStatement preparedStatement = conn.prepareStatement(SQL);
        preparedStatement.setString(1, param);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuilder str = new StringBuilder();
        List<Student> studentList = new ArrayList<>();

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
    public void handleClientMessageSearch(Connection conn, String param) throws SQLException, IOException {
        List<Student> studentByIdList = findById("", conn, param, new Student());
        List<Student> searchStudents;

        if(Objects.equals(param, "all")){
            param = "";
            searchStudents = searchStudent("", conn, param, new Student());
            if(!searchStudents.isEmpty()){
                messageHandler.sendMessageToClient(searchStudents.toString());
                return;
            }
        }
        if(param.isEmpty()){
            return;
        }
        try{
            if(!studentByIdList.isEmpty()){
                messageHandler.sendMessageToClient(studentByIdList.toString());
            }
            return;
        }catch (Exception error){
            System.err.println("studentByIdList: " + error);
        }
        searchStudents = searchStudent("", conn, param, new Student());
        if(!searchStudents.isEmpty()){
            messageHandler.sendMessageToClient(searchStudents.toString());
        }
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
    public List<Student> findById(String SQL, Connection conn, String param, Student student) throws SQLException, IOException {

        System.err.println("running...");
        SQL = "SELECT * FROM students WHERE students.code = ?";
        List <Student> studentList = new ArrayList<>();
        try {
            PreparedStatement execute = conn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            execute.setString(1, param);
            ResultSet resultSet = execute.executeQuery();

            if(resultSet.next()){
                resultSet.beforeFirst();

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String fullname = resultSet.getString("fullname");
                    String birthday = resultSet.getString("birthday");
                    String code = resultSet.getString("code");
                    studentList.add(new Student(id, fullname, birthday, code));
                }
                resultSet.close();
                execute.close();
                System.out.println(studentList);
                return studentList;
            }
        }catch (Exception error){
            System.err.println("ERROR database: " + error);
        }
        return null;
    }

    @Override
    public List<Student> searchStudent(String SQL, Connection conn, String param, Student student) throws SQLException, IOException {

        List <Student> studentList = new ArrayList<>();
        List <Student> studentStaticList = studentRepository.studentStaticRepositories();
        StringBuilder query = new StringBuilder("SELECT * FROM students WHERE");
        query.append(" students.fullname LIKE ?");
        try {
            PreparedStatement execute = conn.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int index = 1;
            execute.setString(index++, "%" + param + "%");
            ResultSet resultSet = execute.executeQuery();

            if(resultSet.next()){
                resultSet.beforeFirst();
                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String fullname = resultSet.getString("fullname");
                    String birthday = resultSet.getString("birthday");
                    String code = resultSet.getString("code");
                    studentList.add(new Student(id, fullname, birthday, code));
                }
                resultSet.close();
                execute.close();
            }
            return studentList;
        }catch (Exception error){
            System.err.println("ERROR database: " + error);
        }
        return filterStudent(studentStaticList, param);
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
    public List<Student> filterStudent(List<Student> studentList, String param) {
        if(param.isEmpty()){
            return studentList;
        }
        return studentList.stream()
            .filter(student -> student.getCode()
            .equalsIgnoreCase(param) || student.getFullname()
            .equalsIgnoreCase(param))
            .collect(Collectors.toList());
    }

    @Override
    public void reloads(DefaultTableModel tableModel) {

        Connection conn = dbConfig.connect_database();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM students";
                Statement execute = conn.createStatement();
                ResultSet data = execute.executeQuery(sql);

                ResultSetMetaData metaData = data.getMetaData();
                int columnCount = metaData.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = metaData.getColumnName(i);
                }
                tableModel.setColumnIdentifiers(columnNames);

                while (data.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = data.getObject(i);
                    }
                    tableModel.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            String[] columnNames = {"id", "fullname", "birthday", "code"};
            List <Student> studentStaticList = studentRepository.studentStaticRepositories();

            tableModel.setColumnIdentifiers(columnNames);
            for (Student student : studentStaticList) {
                Object[] row = new Object[columnNames.length];
                row[0] = student.getId();
                row[1] = student.getFullname();
                row[2] = student.getBirthday();
                row[3] = student.getCode();
                tableModel.addRow(row);
            }
        }
    }

    @Override
    public void refresh(DefaultTableModel tableModel) {
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

    @Override
    public StringBuilder search(String SQL, Connection conn, String param1, String param2, String param3) throws SQLException, IOException {
        return null;
    }

    @Override
    public void handleMessage(String message) {}

    @Override
    public void listenForMessages() {}

    @Override
    public void sendMessageToClient(String response) {}

    @Override
    public void createStudentEvent(DefaultTableModel tableModel, JTextField idField, JTextField fullnameField, JTextField birthdayField, Runnable runnable) {}

    @Override
    public void updateStudentEvent(DefaultTableModel tableModel, JTextField idField, JTextField fullnameField, JTextField birthdayField, JTable studentTable, Runnable runnable) {}

    @Override
    public void deleteStudentEvent(DefaultTableModel tableModel, JTextField idField, JTable studentTable, Runnable runnable) {}

    @Override
    public void searchEvent(JTextField searchField) {}
}
