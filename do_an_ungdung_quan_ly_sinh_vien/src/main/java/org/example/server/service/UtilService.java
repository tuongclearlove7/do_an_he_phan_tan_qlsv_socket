package org.example.server.service;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface UtilService extends MessageService {

    void reloads(DefaultTableModel tableModel);

    void refesh(DefaultTableModel tableModel);

    StringBuilder findById(String SQL, Connection conn, String param) throws SQLException, IOException;

    StringBuilder search(String SQL, Connection conn, String param1, String param2, String param3) throws SQLException, IOException;
    StringBuilder search(String SQL, Connection conn, String param1, String param2) throws SQLException, IOException;
    StringBuilder search(String SQL, Connection conn, String param1) throws SQLException, IOException;

}
