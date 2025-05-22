/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import model.AuthResult;
import enums.UserRole;
import model.LoginCredential;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import model.RegisterCredential;
import rmi.AuthInterface;
import model.AuthToken;
import model.User;
import utils.TokenStorage;

public class AuthClient extends ClientManager<AuthInterface> {

    // handle all send to server and get reponse
    private final String bindObjectName = "AuthService";

    private LoginCredential loginCredential;
    private RegisterCredential registerCredential;

//    public AuthClient(_LoginCredential loginCredential) {
//        this.loginCredential = loginCredential;
//    }
    public AuthClient() {

    }

    public void setLoginCredential(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }

    public void setRegisterCredential(RegisterCredential registerCredential) {
        this.registerCredential = registerCredential;
    }

    private boolean checkRegisterCredential() {
        return this.registerCredential == null;
    }

    private boolean checkLoginCredential() {
        return this.loginCredential == null;
    }

    public RegisterCredential getRegisterCredential() {
        return registerCredential;
    }

    @Override
    protected String getBindObject() {
        return this.bindObjectName;
    }

    // for login
//    private AuthToken sendCrentialToServer() throws RemoteException, NotBoundException, MalformedURLException {
//        AuthInterface Obj = (AuthInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/handleLogin");
//        // get back response from server
//        return Obj.handleLogin(loginCredential);
//    }
    public AuthResult handleAuthLogin(boolean isRememberMe) {
        if (checkLoginCredential()) {
            return new AuthResult("Something went wrong");
        }

        try {

            AuthToken token = connectToServer().handleLogin(loginCredential);
            // credential valid and return a token
            if (token != null) {
                if (isRememberMe) {
                    if (!TokenStorage.saveToken(token)) {
                        return new AuthResult("Error saving Authentication token");
                    }
                }

                UserRole userRole = connectToServer().getUserRoleByToken(token);
                return new AuthResult(userRole, token);
            } else {
                return new AuthResult("Credential Invalid");
            }
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return new AuthResult("Something Went Wrong");
        }
    }

//    // register
//    private AuthToken sendUserToServer() throws RemoteException, NotBoundException, MalformedURLException {
//        AuthInterface Obj = (AuthInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/handleRegister");
//        // get back response from server
//        return Obj.handleRegister(registerCredential);
//    }
    public AuthResult handleAuthRegister() {
        if (checkRegisterCredential()) {
            return new AuthResult("Something went wrong");
        }

        //client side register validation
        try {
            AuthResult response = connectToServer().handleRegister(registerCredential);
            // credential valid and return a token

            return response;
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return new AuthResult("Something Went Wrong");
        }
    }

//    private static void saveToken(String token) throws IOException {
//        try (FileWriter writer = new FileWriter("token.dat")) {
//            writer.write(token);
//        }
//    }
//
//    public static void removeToken() throws IOException {
//        File tokenFile = new File("token.dat");
//        if (tokenFile.exists()) {
//            if (!tokenFile.delete()) {
//                throw new IOException("Failed to delete token file: " + tokenFile.getAbsolutePath());
//            }
//        }
//    }
//
//    private static String loadToken() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("token.dat"))) {
//            return reader.readLine();
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
    public User validateSession(AuthToken token) {
        try {
            if (connectToServer().verifyToken(token)) {
                var user = connectToServer().getUserByToken(token);
                if (user != null) {
                    return user;
                }
                return null;
            } else {
                TokenStorage.deleteToken(); // expired session
            }
            return null;
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return null;
        }
    }

//    // further enhance, make token into a class and serialize it
//    private static boolean sendTokenToServer(AuthToken token) throws RemoteException, NotBoundException, MalformedURLException {
//        AuthInterface Obj = (AuthInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/verifyToken");
//        // get back response from server
//        return Obj.verifyToken(token);
//    }
//    private static AuthInterface connectToServer() throws RemoteException, NotBoundException, MalformedURLException {
//        AuthInterface Obj = (AuthInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/AuthService");
//        return Obj;
//    }
//    private static User sendTokenToServerAndGetUser(AuthToken token) throws RemoteException, NotBoundException, MalformedURLException {
//        AuthInterface Obj = (AuthInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/verifyToken");
//        // get back response from server
//        return Obj.getUserByToken(token);
//    }
    // depends on if logout process need send data to server, now logout only happen in client
    public String handleAuthLogout(AuthToken token) {
        //send to server
        try {
            var success = connectToServer().handleLogout(token);
            if (!success) {
                return "Something Went Wrong on server";
            }
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return "Something Went Wrong";
        }
        return null;
    }

    public User requestUserByToken(AuthToken token) {
        try {
            User user = connectToServer().getUserByToken(token);
            if (user != null) {
                return user;
            }
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return null;
        }
        return null;
    }
}
