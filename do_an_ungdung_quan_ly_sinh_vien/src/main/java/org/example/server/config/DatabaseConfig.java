package org.example.server.config;

import org.example.server.GUI.StudentGUI;
import org.example.server.service.MessageService;
import org.example.server.service.SocketServerService;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConfig {


    private String connectString = EnvConfig.getDatabaseUrl();
    private String username = EnvConfig.getDatabaseUsername();
    private String password = EnvConfig.getDatabasePassword();
    private MessageService messageService;

    public DatabaseConfig(String connectString, String username, String password) {
        this.connectString = connectString;
        this.username = username;
        this.password = password;
    }

    public DatabaseConfig(){
        this.messageService = new MessageService() {};
    }

    public String getConnectString() {
        return connectString;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Connection connect_database() {
        try {
            messageService.initialMessage("\nConnection: " + getConnectString() + " database\n");

            System.out.println("Connection: "+getConnectString());
            return DriverManager.getConnection(getConnectString(), getUsername(), getPassword());
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("ERROR: " + e);
            return null;
        }
    }
}
