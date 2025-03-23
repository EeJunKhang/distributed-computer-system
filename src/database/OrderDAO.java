
package database;

import enums.OrderStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Items;
import model.Order;
import model.OrderItem;
import model.User;

public class OrderDAO {

    public void createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (user_id,, status, total_price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, order.getUser().getUserId());     
            stmt.setString(2, order.getStatus().toString()); 
            stmt.setDouble(3, order.getTotalPrice());     

            stmt.executeUpdate();

        }
    }

    private void insertOrderItem(int orderId, OrderItem item) throws SQLException {
        String sql = "INSERT INTO Order_Items (order_id, product_id, quantity, price_per_unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);              
            stmt.setInt(2, item.getOrderItemId());  
            stmt.setInt(3, item.getQuantity()); 
            stmt.setDouble(4, item.getPricePerUnit());

            stmt.executeUpdate();
        }
    }

    public Order getOrderByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM Orders WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);  
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderTime = rs.getString("order_date");
                String status = rs.getString("status");
                double totalPrice = rs.getDouble("total_price");

                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(userId);

                List<OrderItem> items = getOrderItems(orderId);

                return new Order(orderId, user, orderTime, OrderStatus.valueOf(status), totalPrice, items);
            }
        }
        return null;  
    }

    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        String sql = "SELECT oi.order_item_id, oi.product_id, oi.quantity, oi.price_per_unit, " +
                     "o.order_date, o.status, o.total_price " +
                     "FROM Order_Items oi " +
                     "INNER JOIN Orders o ON o.order_id = oi.order_id " +
                     "WHERE oi.order_id = ?";

        List<OrderItem> items = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId); 
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int orderItemId = rs.getInt("order_item_id");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double pricePerUnit = rs.getDouble("price_per_unit");

                Items item = getItemByProductId(productId);

                Order order = new Order(orderId, null, rs.getString("order_date"),
                                        OrderStatus.valueOf(rs.getString("status")),
                                        rs.getDouble("total_price"), null);

                items.add(new OrderItem(orderItemId, order, item, quantity, pricePerUnit));
            }
        }

        return items;
}

    private Items getItemByProductId(int productId) throws SQLException {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Items(rs.getInt("product_id"), rs.getString("name"), 
                                 rs.getString("description"), rs.getDouble("price"), 
                                 rs.getString("category"), rs.getString("image_url"), 
                                 rs.getInt("stock_quantity"),rs.getString("last_updated"));
            }
        }

        return null; 
}

    public void updateOrderStatus(int orderId, OrderStatus newStatus) throws SQLException {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus.toString()); 
            stmt.setInt(2, orderId);                
            stmt.executeUpdate(); 
        }
    }

    public boolean deleteOrderItem(int orderItemId) throws SQLException {
    String sql = "DELETE FROM Order_Items WHERE order_item_id = ?";

    try (Connection conn = DBConnection.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, orderItemId); 
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;  
    }
}
}

