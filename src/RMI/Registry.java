/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;

/**
 *
 * @author C
 */
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import utils.ConfigReader;

public class Registry {
    public static void main(String args[]) throws RemoteException{
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();

        System.setProperty("java.rmi.server.hostname", serverIP);

        java.rmi.registry.Registry reg = LocateRegistry.createRegistry(rmiPort);
        reg.rebind("handleLogin", new AuthServer());
        
        reg.rebind("handleLogout", new AuthServer());

        System.out.println("Server is running on IP: " + serverIP 
                + " \n Server is bound to port: " + rmiPort);
    }
}