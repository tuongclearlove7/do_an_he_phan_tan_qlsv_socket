package org.example.server.service;

import org.example.server.GUI.StudentGUI;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface SocketServerService {

    void socketInitial() throws IOException;
}
