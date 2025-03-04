/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

/**
 *
 * @author C
 */
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import utils.IPIdentifier;

public class Server extends UnicastRemoteObject implements RMIInterface{
    public Server() throws RemoteException{
        super();
    }

    @Override
    public int add(int x, int y) throws RemoteException {
        String clientIP = IPIdentifier.getClientIP();
        
        System.out.println("Client Called /add from IP: " + clientIP);

        return x + y;
    }
    
}