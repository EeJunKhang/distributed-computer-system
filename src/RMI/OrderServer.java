/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author ejunk
 */
public class OrderServer extends UnicastRemoteObject implements OrderInterface {
    public OrderServer() throws RemoteException {
        super();
    }
}
