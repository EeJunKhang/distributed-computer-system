/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;


import java.rmi.*;

/**
 *
 * @author C
 */
public interface RMIInterface extends Remote{
    public int add(int x, int y) throws RemoteException;
}