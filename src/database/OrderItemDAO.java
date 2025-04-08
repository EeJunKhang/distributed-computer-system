package database;

import model.OrderItem;
import model.Products;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for OrderItem entities
 */
public class OrderItemDAO extends DBOperation<OrderItem, Integer> {

    /**
     * Default constructor
     */
    public OrderItemDAO() {
        super();
    }

    /**
     * Helper method to create an OrderItem object from a ResultSet
     * @param rs The ResultSet
     * @return The OrderItem object
     * @throws SQLException If a database error occurs
     */
    @Override
    protected OrderItem mapResultSetToEntity(ResultSet rs) throws SQLException {
        int orderItemId = rs.getInt("order_item_id");
        int productId = rs.getInt("product_id");
        int quantity = rs.getInt("quantity");
        
        ProductsDAO productsDAO = new ProductsDAO();
        Products product = productsDAO.getProductById(productId);
        
        return new OrderItem(orderItemId, product, quantity);
    }
    
    @Override
    public boolean create(OrderItem orderItem) {
        // This method is not used directly as we always need the orderId
        // Use addOrderItem(int orderId, OrderItem item) instead
        throw new UnsupportedOperationException("Use addOrderItem(int orderId, OrderItem item) instead");
    }
    
    /**
     * Add an order item with order ID
     * @param orderId The order ID
     * @param item The order item to add
     * @return True if successful, false otherwise
     */
    public boolean addOrderItem(int orderId, OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) " +
                     "VALUES (?, ?, ?, ?)";
    
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            System.out.println("Adding order item directly: orderId=" + orderId + 
                                ", productId=" + item.getProduct().getId() + 
                                ", quantity=" + item.getQuantity() + 
                                ", price=" + item.getProduct().getPrice());
            
            stmt.setInt(1, orderId);
            stmt.setInt(2, item.getProduct().getId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getProduct().getPrice());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                System.out.println("No rows affected when adding order item");
                return false;
            }
    
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int itemId = generatedKeys.getInt(1);
                item.setOrderItemId(itemId);
                System.out.println("Order item added with ID: " + itemId);
                return true;
            } else {
                System.out.println("Failed to retrieve generated order item ID");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error adding order item: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    @Override
    public OrderItem read(Integer orderItemId) {
        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderItemId);
                
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
     * @param orderItemId The order item ID
     * @return The order item or null if not found
     */
    public OrderItem getOrderItemById(int orderItemId) {
        return read(orderItemId);
    }

    @Override
    public boolean update(OrderItem orderItem) {
        String sql = "UPDATE order_items SET quantity = ?, price_per_unit = ? WHERE order_item_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderItem.getQuantity());
                stmt.setDouble(2, orderItem.getProduct().getPrice());
                stmt.setInt(3, orderItem.getOrderItemId());
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }
    
    /**
     * Update order item quantity
     * @param orderItemId The order item ID
     * @param quantity The new quantity
     * @return True if successful, false otherwise
     */
    public boolean updateQuantity(int orderItemId, int quantity) {
        String sql = "UPDATE order_items SET quantity = ? WHERE order_item_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, quantity);
                stmt.setInt(2, orderItemId);
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    @Override
    public boolean delete(Integer orderItemId) {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderItemId);
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }
    
    /**
     * Alternative method name for delete - for backward compatibility
     * @param orderItemId The order item ID
     * @return True if successful, false otherwise
     */
    public boolean deleteOrderItem(int orderItemId) {
        return delete(orderItemId);
    }

    @Override
    public List<OrderItem> getAll() {
        String sql = "SELECT * FROM order_items";
        
        return executeTransaction(conn -> {
            List<OrderItem> orderItems = new ArrayList<>();
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    OrderItem orderItem = mapResultSetToEntity(rs);
                    orderItems.add(orderItem);
                }
                
                return orderItems;
            }
        });
    }
    
    /**
     * Get order items by order ID
     * @param orderId The order ID to filter by
     * @return List of order items for the specified order
     */
    public List<OrderItem> getItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        
        return executeTransaction(conn -> {
            List<OrderItem> orderItems = new ArrayList<>();
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, orderId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        OrderItem orderItem = mapResultSetToEntity(rs);
                        orderItems.add(orderItem);
                    }
                    
                    return orderItems;
                }
            }
        });
    }
}