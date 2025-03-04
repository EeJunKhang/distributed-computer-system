/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author C
 */


import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;


public class IPIdentifier {

    public static String getClientIP() {
        try {
            return RemoteServer.getClientHost();
        } catch (ServerNotActiveException ex) {
            System.err.println("Error: Could not retrieve client IP.");
            return "Unknown IP";
        }
    }
}