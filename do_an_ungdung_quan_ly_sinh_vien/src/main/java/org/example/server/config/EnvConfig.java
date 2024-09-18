package org.example.server.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = EnvConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Unable to load file: application.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public EnvConfig() {
    }

    public static String getDatabaseUrl() {
        return properties.getProperty("datasource.url");
    }

    public static String getDatabaseUsername() {
        return properties.getProperty("datasource.username");
    }

    public static String getDatabasePassword() {
        return properties.getProperty("datasource.password");
    }

    public String getPort() {
        return properties.getProperty("port");
    }
}
