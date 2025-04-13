/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Order;
import model.Products;
import utils.IPIdentifier;

/**
 *
 * @author ejunk
 */
public class DashboardServer extends UnicastRemoteObject implements DashboardInterface {
    public DashboardServer() throws RemoteException {
        super();
    }
    
    // make a abstract that this cclass extends
    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("Client Called /add from IP: " + clientIP);
    }

    @Override
    public List<Products> fetchAllProducts() throws RemoteException {
        showClientIP();
        return null;
    }

    @Override
    public List<Order> fetchUserOrder() throws RemoteException {
        showClientIP();
        return null;
    }
}
