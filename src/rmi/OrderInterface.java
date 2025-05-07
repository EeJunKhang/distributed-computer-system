/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Order;
import model.AuthToken;
import enums.OrderStatus;
import java.util.Map;
import model.Payment;
import model.ReportData;

public interface OrderInterface extends Remote {

    /**
     * Gets an order by its ID.
     * @param token Authentication token.
     * @param orderId The ID of the order.
     * @return The Order object or null if not found or unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    Order getOrderById(AuthToken token, int orderId) throws RemoteException;

    /**
     * Gets all orders in the system (admin only).
     * @param token Authentication token.
     * @return List of all orders or null if unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    List<Order> getAllOrders(AuthToken token) throws RemoteException;

    /**
     * Gets all orders for a specific user.
     * @param token Authentication token.
     * @param userId The ID of the user.
     * @return List of orders for the specified user or null if unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    List<Order> getOrdersByUserId(AuthToken token, int userId) throws RemoteException;

    /**
     * Gets all orders for a specific user.
     * @param token Authentication token.
     * @param status
     * @return List of orders for the specified status or null if unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    List<Order> getOrdersByStatus(AuthToken token, OrderStatus status) throws RemoteException;

    /**
     * Creates a new order.
     * @param token Authentication token.
     * @param order The Order object to create.
     * @param isBankPayment
     * @param paymentInfo
     * @return true if the order was created successfully, false otherwise.
     * @throws RemoteException If a remote error occurs.
     */
    boolean createOrder(AuthToken token, Order order, boolean isBankPayment, Map<String, String> paymentInfo) throws RemoteException;

    /**
     * Updates the status of an order (admin only).
     * @param token Authentication token.
     * @param orderId The ID of the order to update.
     * @param status The new status of the order.
     * @return true if the order status was updated successfully, false otherwise.
     * @throws RemoteException If a remote error occurs.
     */
    boolean updateOrderStatus(AuthToken token, int orderId, OrderStatus status) throws RemoteException;

    /**
     * Deletes an order by its ID (admin only).
     * @param token Authentication token.
     * @param orderId The ID of the order to delete.
     * @return true if the order was deleted successfully, false otherwise.
     * @throws RemoteException If a remote error occurs.
     */
    boolean deleteOrder(AuthToken token, int orderId) throws RemoteException;
    
    ReportData getReportData(AuthToken token) throws RemoteException;
    List<Payment> getAllPaymentData(AuthToken token) throws RemoteException;
}
