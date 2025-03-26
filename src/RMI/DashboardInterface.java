/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package RMI;

import java.rmi.*;
import java.rmi.RemoteException;
import java.util.List;
import model.Order;
import model.Products;

public interface DashboardInterface extends Remote {
    public List<Products> fetchAllProducts() throws RemoteException;
    
    public List<Order> fetchUserOrder() throws RemoteException;
}
