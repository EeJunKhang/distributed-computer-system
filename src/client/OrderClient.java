/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import rmi.OrderInterface;

/**
 *
 * @author ejunk
 */
public class OrderClient extends ClientManager<OrderInterface>{
    private final String bindObjectName = "/OrderService";

    @Override
    protected String getBindObject() {
       return this.bindObjectName;
    }
    
}
