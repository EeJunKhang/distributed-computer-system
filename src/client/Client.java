/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author C
 */
import java.rmi.*;
import java.net.MalformedURLException;
import rmi.RMIInterface;
import utils.ConfigReader;

public class Client {
    public static void main(String args[]) throws RemoteException, NotBoundException, MalformedURLException {
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();
        
        RMIInterface Obj = (RMIInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/add");
        
        System.out.println("The number is "+ Obj.add(6, 9));
    }
}