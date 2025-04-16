/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ejunk
 */
public class ImagePathGetter {
    public static String getImageFullPath(String imagePath){
        String imageDirectory;
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/config/UtilityProperties.properties")) {
            properties.load(fis);
            imageDirectory = properties.getProperty("image.directory");
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
            imageDirectory = System.getProperty("user.home") + File.separator + "images"; // Fallback
        }
        return imageDirectory + File.separator + imagePath;
    }
}
