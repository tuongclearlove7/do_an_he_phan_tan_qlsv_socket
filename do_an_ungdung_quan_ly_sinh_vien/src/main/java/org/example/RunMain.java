package org.example;

import org.example.client.GUI.ChatClientStudentGUI;
import org.example.server.GUI.StudentGUI;
import javax.swing.*;
import java.io.IOException;
public class RunMain {

    public static void main(String[] args) throws InterruptedException {
        init(new RunMain());
    }

    public static void init(RunMain app){
        //Init scope
        AppServer(app::sleep, app::AppClient);
    }

    public static void AppServer(Runnable Sleep, Runnable AppClient){
        //Server app scope
        SwingUtilities.invokeLater(() -> {
            try {
                new StudentGUI().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Sleep.run();
        AppClient.run();
    }
    private void AppClient() {
        //Client app scope
        SwingUtilities.invokeLater(() -> {
            new ChatClientStudentGUI().setVisible(true);
        });
    }

    public void sleep() {
        //Delay
        try{
            Thread.sleep(2000);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}