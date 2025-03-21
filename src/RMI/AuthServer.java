/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;

/**
 *
 * @author C
 */
import model.LoginCredential;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import utils.IPIdentifier;
import utils.TokenUtil;

/**
 *
 * @author ejunk
 */
public class AuthServer extends UnicastRemoteObject implements CredentialsInterface {
    public AuthServer() throws RemoteException {
        super();
    }

    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("Client Called /add from IP: " + clientIP);
    }

    @Override
    public String handleLogin(LoginCredential credential) throws RemoteException {
        showClientIP();
        // access username and password like this
        System.out.println(credential.getPassword());
        System.out.println(credential.getUsername());
        // check for db, if user found, validate credentail
        if("a".equals(credential.getPassword()) && "a".equals(credential.getUsername())){
            // generate token with this credential, need change to user id (fetch from db)
            String token = TokenUtil.generateToken(credential);
            return token;
        }
        
        // only return null or token (null = wrong credential, token = ssuccess)
        return null;
    }

    @Override
    public boolean handleLogout() throws RemoteException {
        showClientIP();
        //handle db logout process here
        return true;
    }

    @Override
    public boolean verifyToken(String token) throws RemoteException{
        showClientIP();
        String username = TokenUtil.validateToken(token);
        return username != null;
    }

}
