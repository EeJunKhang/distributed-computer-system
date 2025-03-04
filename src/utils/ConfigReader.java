/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


/**
 *
 * @author C
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try {
            try (FileInputStream fis = new FileInputStream("src/config/NetworkProperties.properties")) {
                properties.load(fis);
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration file.");
        }
    }

    public static String getServerIP() {
        return properties.getProperty("server.ip");
    }

    public static String getClientIP() {
        return properties.getProperty("client.ip");
    }

    public static int getRmiPort() {
        return Integer.parseInt(properties.getProperty("rmi.port"));
    }
}
