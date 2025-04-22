/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import RMI.DashboardInterface;
import static client.Dashboard.rmiPort;
import static client.Dashboard.serverIP;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import utils.ConfigReader;
import model.Products;

/**
 *
 * @author zheng
 */
public class Dashboard {
    public static String serverIP = ConfigReader.getServerIP();
    public static int rmiPort = ConfigReader.getRmiPort();
    
    private Products products;
    
    public Dashboard(){
        
    }
    
    public void setProducts(Products produts) {
        this.products = produts;
    }
    
    private List<Products> getProductsFromServer() throws RemoteException, NotBoundException, MalformedURLException {
        DashboardInterface Obj = (DashboardInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/fetchAllProducts");
        // get back response from server
        return Obj.fetchAllProducts();
    }
    
    public List<Products> handleGetAllData () throws NotBoundException, MalformedURLException{
        try{
            List<Products> response = this.getProductsFromServer();
            if (response!= null){
                return response;
            }
        }catch(RemoteException ex){
            System.out.println(ex);
            
        }
        return new ArrayList<>();
    }
    
    private List<Products> getNewProductsFromServer() throws RemoteException, NotBoundException, MalformedURLException {
        DashboardInterface Obj = (DashboardInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/fetchNewProducts");
        // get back response from server
        return Obj.fetchNewProducts();
    }
    
    public List<Products> handleGetAllNewData () throws NotBoundException, MalformedURLException{
        try{
            List<Products> response = this.getNewProductsFromServer();
            if (response!= null){
                return response;
            }
        }catch(RemoteException ex){
            System.out.println(ex);
            
        }
        return new ArrayList<>();
    }
    
    private List<Products> getHotProductsFromServer() throws RemoteException, NotBoundException, MalformedURLException {
        DashboardInterface Obj = (DashboardInterface) Naming.lookup("rmi://" + serverIP + ":" + rmiPort + "/fetchHotProducts");
        // get back response from server
        return Obj.fetchHotProducts();
    }
    
    public List<Products> handleGetHotProducts() throws NotBoundException, MalformedURLException{
        try{
            List<Products> response = this.getHotProductsFromServer();
            if (response!= null){
                return response;
            }
        }catch(RemoteException ex){
            System.out.println(ex);
            
        }
        return new ArrayList<>();
    }
    
}
