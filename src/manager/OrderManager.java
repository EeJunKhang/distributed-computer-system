/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author C
 */

package manager;

import database.OrderDAO;
import database.PaymentDAO;
import model.Order;
import enums.OrderStatus;
import enums.PaymentStatus;
import java.util.List;
import model.Payment;

/**
 * Manager class for Order-related operations
 */
public class OrderManager {

    private static OrderManager instance;
    private final OrderDAO orderDAO;
    private final PaymentDAO paymentDAO;

    private OrderManager() {
        this.orderDAO = new OrderDAO();
        this.paymentDAO = new PaymentDAO();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Gets an order by its ID.
     * @param orderId The ID of the order.
     * @return The Order object or null if not found.
     */
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }

    /**
     * Gets all orders in the system.
     * @return List of all orders.
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    /**
     * Gets all orders for a specific user.
     * @param userId The ID of the user.
     * @return List of orders for the specified user.
     */
    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.getOrdersByUserId(userId);
    }

    /**
     * Gets all orders with a specific status.
     * @param status The status to filter by.
     * @return List of orders with the specified status.
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderDAO.getOrdersByStatus(status);
    }

    /**
     * Creates a new order.
     * @param order The Order object to create.
     * @return true if the order was created successfully, false otherwise.
     */
    public Integer createOrder(Order order) {
        return orderDAO.createOrder(order);
    }

    /**
     * Updates the status of an order.
     * @param orderId The ID of the order to update.
     * @param status The new status of the order.
     * @return true if the order status was updated successfully, false otherwise.
     */
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }

    /**
     * Deletes an order by its ID.
     * @param orderId The ID of the order to delete.
     * @return true if the order was deleted successfully, false otherwise.
     */
    public boolean deleteOrder(int orderId) {
        return orderDAO.deleteOrder(orderId);
    }
    
    public List<Payment> getAllPayment(){
        return paymentDAO.getAll();
    }
    
    public Integer createPayment(Payment payment){
        return paymentDAO.createPayment(payment);
    }
    
    public boolean updatePaymentStatus(int paymentId, PaymentStatus paymentStatus){
        return paymentDAO.updatePaymentStatus(paymentId, paymentStatus);
    }
}
