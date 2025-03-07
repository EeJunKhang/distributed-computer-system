/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;

/**
 *
 * @author C
 */
import client.LoginCredential;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import utils.IPIdentifier;

/**
 *
 * @author ejunk
 */
public class AuthServer extends UnicastRemoteObject implements CredentialsInterface {

    public AuthServer() throws RemoteException {
        super();
        String clientIP = IPIdentifier.getClientIP();

        System.out.println("Client Called /add from IP: " + clientIP);
    }

    @Override
    public boolean handleLogin(LoginCredential credential) throws RemoteException {
        

        // check for db, if user found
        // only return true or false (false = wrong credential)
        return true;
    }
    
    @Override
    public boolean handleLogout() throws RemoteException {
        //handle db logout process here
        return true;
    }

}
