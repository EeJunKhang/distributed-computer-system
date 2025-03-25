/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import model.OrderItem;
import java.sql.*;
import java.util.List;
import model.Products;

public class OrderItemDAO extends DBOperation<OrderItem> {

    public OrderItemDAO() {
        super("order_items", "order_id", "product_id", "quantity", "price_per_unit");
    }

    @Override
    protected OrderItem mapResultSetToEntity(ResultSet rs) throws SQLException {
        ProductsDAO productsDAO = new ProductsDAO();
        Products product = productsDAO.getProductById(rs.getInt("product_id"));

        return new OrderItem(
            rs.getInt("order_item_id"),
            product,
            rs.getInt("quantity"),
            rs.getDouble("price_per_unit")
        );
    }

    public void addOrderItem(int orderId, int productId, int quantity, double pricePerUnit) throws SQLException {
        insert(null, orderId, productId, quantity, pricePerUnit);
    }

    public List<OrderItem> getItemsByOrderId(int orderId) throws SQLException {
        return searchByColumn("order_id", orderId,false);
    }

    public void updateOrderItem(int orderItemId, int orderId,int productId, int quantity, double pricePerUnit) throws SQLException {
        update("order_item_id",orderItemId,orderId, productId, quantity, pricePerUnit);
    }

    public void deleteOrderItem(int orderItemId) throws SQLException {
        deleteById(orderItemId, "order_item_id");
    }
}

