/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import model.AuthToken;
import rmi.UserInterface;

/**
 *
 * @author ejunk
 */
public class UserClient extends ClientManager<UserInterface> {

    private final String bindObjectName = "UserService";
//    private Order order;
    @SuppressWarnings("unused")
    private AuthToken token;

    @Override
    protected String getBindObject() {
        return this.bindObjectName;
    }

    public UserClient(AuthToken token) {
        this.token = token;
    }

    public boolean updateProfile(AuthToken token, int userId, String firstName,
                                        String lastName, String email, String address, String username,
                                        String contactNumber) {
        try {
            return connectToServer().updateUserProfile(token, userId, firstName, lastName, email, address, username, contactNumber);
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return false;
        }
    }

    
}
