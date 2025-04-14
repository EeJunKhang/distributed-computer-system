/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import rmi.ProductInterface;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import model.AuthToken;
import model.User;

/**
 *
 * @author ejunk
 */
public class ProductClient extends ClientManager<ProductInterface> {
    private final String bindObjectName = "/ProductService";
    
    public ProductClient(){
        
    }

    @Override
    protected String getBindObject() {
        return this.bindObjectName;
    }
    
    
   
    
}
