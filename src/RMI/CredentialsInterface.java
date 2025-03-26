/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package RMI;

import java.rmi.*;
import model.LoginCredential;
import java.rmi.RemoteException;
import model.Customer;
import model.RegisterCredential;

public interface CredentialsInterface extends Remote {

    public String handleLogin(LoginCredential loginCredentialsBytes) throws RemoteException;

    public boolean handleLogout() throws RemoteException;
    
    public String handleRegister(RegisterCredential RegisterCredentialsBytes) throws RemoteException;
    
    public boolean verifyToken(String token) throws RemoteException;
}
