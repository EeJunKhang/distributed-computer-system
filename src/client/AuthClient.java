/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import model.LoginCredential;
import RMI.CredentialsInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import utils.ConfigReader;

public class AuthClient {

    // handle all send to server and get reponse
    public static String serverIP = ConfigReader.getServerIP();
    public static int rmiPort = ConfigReader.getRmiPort();

    private final LoginCredential loginCredential;

    public AuthClient(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }

    // for login
    private String sendCrentialToServer() throws RemoteException, NotBoundException, MalformedURLException {
        CredentialsInterface Obj = (CredentialsInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/handleLogin");
        // get back response from server
        return Obj.handleLogin(loginCredential);
    }

    public String handleAuthLogin(boolean isRememberMe) {
        try {
            var response = this.sendCrentialToServer();
            // credential valid and return a token
            if (response != null) {
                if (isRememberMe) {
                    try {
                        //store token in local if user choose remember me
                        saveToken(response);
                    } catch (IOException ex) {
                        System.out.println(ex);
                        return "Error saving token";
                    }
                }
                return null;
            } else {
                return "Credential Invalid";
            }
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            System.out.println(ex);
            return "Something Went Wrong";
        }
    }

    private static void saveToken(String token) throws IOException {
        try (FileWriter writer = new FileWriter("token.dat")) {
            writer.write(token);
        }
    }
    
    public static void removeToken() throws IOException {
        File tokenFile = new File("token.dat");
        if(tokenFile.exists()){
            if(!tokenFile.delete()){
                throw new IOException("Failed to delete token file: "+ tokenFile.getAbsolutePath());
            }
        }
    }

    private static String loadToken() {
        try (BufferedReader reader = new BufferedReader(new FileReader("token.dat"))) {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean verifyToken() {
        String token = loadToken(); // get saved token from local
        try {
            return token != null && sendTokenToServer(token);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    // further enhance, make token into a class and serialize it
    private static boolean sendTokenToServer(String token) throws RemoteException, NotBoundException, MalformedURLException {
        CredentialsInterface Obj = (CredentialsInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/verifyToken");
        // get back response from server
        return Obj.verifyToken(token);
    }
    
    // depends on if logout process need send data to server, now logout only happen in client
    public boolean handleAuthLogout() {
        //send to server
        return true;
    }

}
