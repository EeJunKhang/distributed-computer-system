/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serialization {
    private final Object object;

    public Serialization(Object object) {
        this.object = object;
    }
    
    public boolean serialize() {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream("local.ser");
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(object);

            out.close();
            file.close();

            return true;
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            return false;
        }
    }

    public Object deserialize() {
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("local.ser"); // find a way to hanlde file name
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            var object1 = in.readObject();

            in.close();
            file.close();

            return object1;
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            return null;
        }
    }
}
