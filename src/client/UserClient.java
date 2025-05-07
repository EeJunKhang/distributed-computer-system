/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import enums.OrderStatus;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import model.AuthToken;
import model.Order;
import model.Payment;
import model.ReportData;
import rmi.UserInterface;

/**
 *
 * @author ejunk
 */
public class UserClient extends ClientManager<UserInterface> {

    private final String bindObjectName = "UserService";
//    private Order order;
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
