package database;

import enums.OrderStatus;
import model.Order;
import model.Customer;
import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Order entities
 */
public class OrderDAO extends DBOperation<Order, Integer> {

    /**
     * Default constructor
     */
    public OrderDAO() {
        super();
    }

    /**
     * Helper method to create an Order object from a ResultSet
     *
     * @param rs The ResultSet
     * @return The Order object
     * @throws SQLException If a database error occurs
     */
    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        int orderId = rs.getInt("order_id");
        int userId = rs.getInt("user_id");
        String orderTime = rs.getString("order_time");
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));
        double totalPrice = rs.getDouble("total_price");
        
        UserDAO userDAO = new UserDAO();
        Object user = userDAO.getUserById(userId);

        Customer customer;
        if (user instanceof Customer) {
            customer = (Customer) user;
        } else {
            // case where user is not a Customer
            // create a placeholder customer or skip this order
            System.out.println("Warning: User ID " + userId + " is not a Customer. Creating placeholder.");
            customer = new Customer();
            customer.setUserId(userId);
        }

        OrderItemDAO orderItemDAO = new OrderItemDAO();
        List<OrderItem> items = orderItemDAO.getItemsByOrderId(orderId);

        return new Order(orderId, customer, orderTime, status, totalPrice, items);
    }

    @Override
    public boolean create(Order order) {
        String sql = "INSERT INTO orders (user_id, order_time, status, total_price) "
                + "VALUES (?, NOW(), ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, order.getUser().getUserId());
            stmt.setString(2, order.getStatus().toString());
            stmt.setDouble(3, order.getTotalPrice());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows affected in the orders table.");
                return false;
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);
                order.setOrderId(orderId);

                System.out.println("Order created with ID: " + orderId);

                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }

                OrderItemDAO orderItemDAO = new OrderItemDAO();
                System.out.println("Adding " + order.getItems().size() + " order items");

                boolean allItemsInserted = true;

                for (OrderItem item : order.getItems()) {
                    System.out.println("Inserting order item for product ID: " + item.getProduct().getId());
                    boolean itemInserted = orderItemDAO.addOrderItem(orderId, item);

                    if (!itemInserted) {
                        System.out.println("Failed to insert item for product ID: " + item.getProduct().getId());
                        allItemsInserted = false;
                    }
                    System.out.println("Item insertion completed for product ID: " + item.getProduct().getId());
                }

                System.out.println("All items processed, success: " + allItemsInserted);
                return true;
            } else {
                System.out.println("Failed to retrieve generated order ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    public Integer createOrder(Order order){
        String sql = "INSERT INTO orders (user_id, order_time, status, total_price) "
                + "VALUES (?, NOW(), ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, order.getUser().getUserId());
            stmt.setString(2, order.getStatus().toString());
            stmt.setDouble(3, order.getTotalPrice());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("No rows affected in the orders table.");
                return null;
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);
                order.setOrderId(orderId);

                System.out.println("Order created with ID: " + orderId);

                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }

                OrderItemDAO orderItemDAO = new OrderItemDAO();
                System.out.println("Adding " + order.getItems().size() + " order items");

                boolean allItemsInserted = true;

                for (OrderItem item : order.getItems()) {
                    System.out.println("Inserting order item for product ID: " + item.getProduct().getId());
                    boolean itemInserted = orderItemDAO.addOrderItem(orderId, item);

                    if (!itemInserted) {
                        System.out.println("Failed to insert item for product ID: " + item.getProduct().getId());
                        allItemsInserted = false;
                    }
                    System.out.println("Item insertion completed for product ID: " + item.getProduct().getId());
                }

                System.out.println("All items processed, success: " + allItemsInserted);
                return orderId;
            } else {
                System.out.println("Failed to retrieve generated order ID.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Alternative method name for create - for backward compatibility
     *
     * @param order The order to add
     * @return True if successful, false otherwise
     */
    public boolean addOrder(Order order) {
        return create(order);
    }

    @Override
    public Order read(Integer orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                    return null;
                }
            }
        });
    }

    /**
     * Alternative method name for read - for backward compatibility
     *
     * @param orderId The order ID
     * @return The order or null if not found
     */
    public Order getOrderById(int orderId) {
        return read(orderId);
    }

    @Override
    public boolean update(Order order) {
        String sql = "UPDATE orders SET status = ?, total_price = ? WHERE order_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, order.getStatus().toString());
                stmt.setDouble(2, order.getTotalPrice());
                stmt.setInt(3, order.getOrderId());

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    /**
     * Update order status
     *
     * @param orderId The order ID
     * @param status The new status
     * @return True if successful, false otherwise
     */
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, status.toString());
                stmt.setInt(2, orderId);

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    @Override
    public boolean delete(Integer orderId) {
        return executeTransaction(conn -> {
            try {
                String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";
                try (PreparedStatement itemsStmt = conn.prepareStatement(deleteItemsSql)) {
                    itemsStmt.setInt(1, orderId);
                    itemsStmt.executeUpdate();
                }

                String deleteOrderSql = "DELETE FROM orders WHERE order_id = ?";
                try (PreparedStatement orderStmt = conn.prepareStatement(deleteOrderSql)) {
                    orderStmt.setInt(1, orderId);
                    int affectedRows = orderStmt.executeUpdate();
                    return affectedRows > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Alternative method name for delete - for backward compatibility
     *
     * @param orderId The order ID
     * @return True if successful, false otherwise
     */
    public boolean deleteOrder(int orderId) {
        return delete(orderId);
    }

    @Override
    public List<Order> getAll() {
        String sql = "SELECT * FROM orders";

        return executeTransaction(conn -> {
            List<Order> orders = new ArrayList<>();

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Order order = mapResultSetToEntity(rs);
                    orders.add(order);
                }

                return orders;
            }
        });
    }

    /**
     * Alternative method name for getAll - for backward compatibility
     *
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        return getAll();
    }

    /**
     * Get orders by user ID
     *
     * @param userId The user ID to filter by
     * @return List of orders for the specified user
     */
    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";

        return executeTransaction(conn -> {
            List<Order> orders = new ArrayList<>();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Order order = mapResultSetToEntity(rs);
                        orders.add(order);
                    }

                    return orders;
                }
            }
        });
    }

    /**
     * Get orders by status
     *
     * @param status The status to filter by
     * @return List of orders with the specified status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        String sql = "SELECT * FROM orders WHERE status = ?";

        return executeTransaction(conn -> {
            List<Order> orders = new ArrayList<>();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, status.toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Order order = mapResultSetToEntity(rs);
                        orders.add(order);
                    }

                    return orders;
                }
            }
        });
    }
}
