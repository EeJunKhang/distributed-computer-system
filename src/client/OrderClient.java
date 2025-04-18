/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import model.AuthToken;
import model.Order;
import rmi.OrderInterface;

/**
 *
 * @author ejunk
 */
public class OrderClient extends ClientManager<OrderInterface>{
    private final String bindObjectName = "/OrderService";
    private Order order;
    
    @Override
    protected String getBindObject() {
       return this.bindObjectName;
    }
    
    public OrderClient(){
        
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
    
    public boolean handleOrder(AuthToken token){
        try {
            return connectToServer().createOrder(token, order);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    public List<Order> fetchAllOrders(AuthToken token){
        try {
            return connectToServer().getAllOrders(token);
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            System.out.println(ex);
            return null;
        }
    }
}
