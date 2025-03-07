/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import RMI.CredentialsInterface;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import utils.ConfigReader;
import utils.Serialization;

public class AuthClient {

    // handle all send to server and get reponse
    public static String serverIP = ConfigReader.getServerIP();
    public static int rmiPort = ConfigReader.getRmiPort();

    private final LoginCredential loginCredential;

    public AuthClient(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }

    // for login
    private boolean sendCrentialToServer() throws RemoteException, NotBoundException, MalformedURLException {
        CredentialsInterface Obj = (CredentialsInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/handleLogin");

        boolean response = Obj.handleLogin(loginCredential);
        return response;
    }

    public String handleAuthLogin() {
        Serialization ser = new Serialization(loginCredential);
        var isSerSuccess = ser.serialize();
        if (isSerSuccess) {
            var isDesSuccess = ser.deserialize();
            if (isDesSuccess != null) {
                try {
                    var response = this.sendCrentialToServer();
                    if (response) {
                        return null;
                    } else {
                        return "Credential Invalid";
                    }
                } catch (RemoteException | NotBoundException | MalformedURLException ex) {
                    System.out.println(ex);
                    return "Something Went Wrong";
                }
            }
            return "Deserialization Failed";
        }
        return "Serialization Failed";
    }
}
