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
import model.Order;
import model.User;
import enums.OrderStatus;
import java.util.List;

/**
 * Manager class for Order-related operations
 */
public class OrderManager {

    private static OrderManager instance;
    private final OrderDAO orderDAO;

    private OrderManager() {
        this.orderDAO = new OrderDAO();
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
     * Creates a new order.
     * @param order The Order object to create.
     * @return true if the order was created successfully, false otherwise.
     */
    public boolean createOrder(Order order) {
        return orderDAO.addOrder(order);
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
}