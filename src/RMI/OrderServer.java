/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// RMI/OrderServer.java
package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import manager.AuthManager;
import manager.OrderManager;
import model.AuthToken;
import model.Order;
import model.User;
import enums.OrderStatus;
import utils.IPIdentifier;

public class OrderServer extends UnicastRemoteObject implements OrderInterface {

    private final OrderManager orderManager;
    private final AuthManager authManager;

    public OrderServer() throws RemoteException {
        super();
        this.orderManager = OrderManager.getInstance();
        this.authManager = AuthManager.getInstance();
    }

    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("Order Service called from IP: " + clientIP);
    }

    private User validateTokenAndGetUser(AuthToken token) {
        if (token == null) {
            return null;
        }
        return authManager.getUserByToken(token.getToken());
    }

    @Override
    public Order getOrderById(AuthToken token, int orderId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getOrderById from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getOrderById(orderId);
    }

    @Override
    public List<Order> getAllOrders(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != enums.UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for getAllOrders from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getAllOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(AuthToken token, int userId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getUserId() != userId) {
            System.out.println("Authentication failed or unauthorized for getOrdersByUserId from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getOrdersByUserId(userId);
    }

    @Override
    public boolean createOrder(AuthToken token, Order order) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getUserId() != order.getUser().getUserId()) {
            System.out.println("Authentication failed or unauthorized for createOrder from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.createOrder(order);
    }

    @Override
    public boolean updateOrderStatus(AuthToken token, int orderId, OrderStatus status) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != enums.UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for updateOrderStatus from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean deleteOrder(AuthToken token, int orderId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != enums.UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for deleteOrder from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.deleteOrder(orderId);
    }
}