/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
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
    private final String bindObjectName = "OrderService";
    private Order order;
    private AuthToken token;
    
    @Override
    protected String getBindObject() {
       return this.bindObjectName;
    }
    
    public OrderClient(AuthToken token) {
        this.token = token;
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
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    public List<Order> fetchAllOrders(AuthToken token){
        try {
            return connectToServer().getAllOrders(token);
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return null;
        }
    }
    
    public List<Order> fetchOrderByUserId(AuthToken token,int userId){
        try{
            
            return connectToServer().getOrdersByUserId(token,userId);
        }catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException ex) {
            System.out.println(ex);
            return null;
        }
    }
}
