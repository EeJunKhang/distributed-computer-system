
// RMI/OrderServer.java
package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import manager.AuthManager;
import manager.OrderManager;
import model.AuthToken;
import model.Order;
import model.User;
import enums.OrderStatus;
import model.Payment;
import enums.UserRole;
import model.ReportData;
import rmi.OrderInterface;
import security.RMISSLClientSocketFactory;
import security.RMISSLServerSocketFactory;
import utils.ConfigReader;
import utils.IPIdentifier;

public class OrderServer extends UnicastRemoteObject implements OrderInterface {

    private final OrderManager orderManager;
    private final AuthManager authManager;

    public OrderServer() throws RemoteException, Exception {
        super(ConfigReader.getRmiPort(), new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
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
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
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
    public List<Order> getOrdersByStatus(AuthToken token, OrderStatus status) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for getOrdersByStatus from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getOrdersByStatus(status);
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
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for updateOrderStatus from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean deleteOrder(AuthToken token, int orderId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for deleteOrder from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.deleteOrder(orderId);
    }
    
    
    @Override
    public ReportData getReportData(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN){
            
            return null;
        }
        var orderList = orderManager.getAllOrders();
        double totalSales = 0;
        int totalItem = 0;
        int totalOrder = orderList.size();
        for(var i : orderList){
            totalSales = totalSales + i.getTotalPrice();
            totalItem =  totalItem + i.getItems().size();
        }
        
        double average = totalSales / totalOrder;
//        average = Math.round(average * 100.0) / 100.0;
        return new ReportData(totalSales, totalOrder,totalItem, average);
    }
    
    @Override
    public List<Payment> getAllPaymentData(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != enums.UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for deleteOrder from " + IPIdentifier.getClientIP());
            return null;
        }
        System.out.println(" failed or unauthorized for deleteOrder from " + IPIdentifier.getClientIP());
        return orderManager.getAllPayment();
    }
}
