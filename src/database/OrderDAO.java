package database;

import enums.OrderStatus;
import java.sql.*;
import java.util.List;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.User;

public class OrderDAO extends DBOperation<Order> {

    public OrderDAO() {
        super("orders", "user_id", "status", "total_price");
    }

    @Override
    protected Order mapResultSetToEntity(ResultSet rs) throws SQLException {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserById(rs.getInt("user_id"));
        Customer cus = new Customer(user.getUserId(),user.getFirstName(), user.getLastName(), user.getUsername(), user.getPasswordHash(), user.getEmail(), user.getAddress(), user.getContactNumber(), user.getCreatedTime());
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        List<OrderItem> items = orderItemDAO.getItemsByOrderId(rs.getInt("order_id"));

        return new Order(
            rs.getInt("order_id"),
            cus,
            rs.getString("order_date"),
            OrderStatus.valueOf(rs.getString("status")),
            rs.getDouble("total_price"),
            items
        );
    }

    public void addOrder(int userId, OrderStatus status, double totalPrice) throws SQLException {
        insert(null, userId, status.name(), totalPrice); // 'order_id' is auto-incremented, so passing null
    }

    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        return searchByColumn("user_id", userId,false);
    }

    public void updateOrder(int orderId,int userId, OrderStatus status, double totalPrice) throws SQLException {
        update("order_id",orderId,userId, status.name(), totalPrice);
    }
}

