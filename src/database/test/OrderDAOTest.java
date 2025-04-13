package database.test;

import database.OrderDAO;
import database.ProductsDAO;
import database.UserDAO;
import model.Order;
import model.OrderItem;
import model.Products;
import model.Customer;
import enums.OrderStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOTest {
    
    public static void main(String[] args) {
        testOrderDAO();
    }
    
    public static void testOrderDAO() {
        // First, manually verify the products exist
        Connection conn = null;
        try {
            // Replace with your actual database connection details
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/food_ordering_database", "root", "root");
            
            // Check product 1
            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM products WHERE product_id = ?");
            stmt1.setInt(1, 1);
            ResultSet rs1 = stmt1.executeQuery();
            boolean product1Exists = rs1.next();
            System.out.println("Product 1 exists: " + product1Exists);
            rs1.close();
            stmt1.close();
            
            // Check product 2
            PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM products WHERE product_id = ?");
            stmt2.setInt(1, 2);
            ResultSet rs2 = stmt2.executeQuery();
            boolean product2Exists = rs2.next();
            System.out.println("Product 2 exists: " + product2Exists);
            rs2.close();
            stmt2.close();
            
            conn.close();
            
            if (!product1Exists || !product2Exists) {
                System.out.println("WARNING: One or both products don't exist!");
            }
        } catch (SQLException e) {
            System.out.println("Error checking products: " + e.getMessage());
        }
        OrderDAO orderDAO = new OrderDAO();
        UserDAO userDAO = new UserDAO();
        ProductsDAO productsDAO = new ProductsDAO();

        // Then, get a customer for the order
        Customer customer = (Customer) userDAO.getUserById(1); // Replace with a valid user ID
        if (customer == null) {
            System.out.println("No customer found with ID 1. Please use a valid customer ID.");
            return;
        }
        
        // Create some order items (assuming products with IDs 1 and 2 exist)
        List<OrderItem> items = new ArrayList<>();
        
        // In a real scenario, you would get actual products from the database
        // For this test, we'll create simple order items
        Products product1 = new Products();
        product1.setId(1);
        product1.setPrice(29.99);
        
        Products product2 = new Products();
        product2.setId(2);
        product2.setPrice(19.99);
        
        items.add(new OrderItem(product1, 2)); // 2 of product 1
        items.add(new OrderItem(product2, 1)); // 1 of product 2
        
        // Calculate total price
        double totalPrice = (product1.getPrice() * 2) + (product2.getPrice() * 1);
        
        // Test create order
        System.out.println("Testing create order:");
        Order newOrder = new Order(customer, OrderStatus.PENDING, totalPrice, items);
        boolean created = orderDAO.create(newOrder);
        System.out.println("Order created: " + created);
        System.out.println("Order ID: " + newOrder.getOrderId());
        
        // Test read order
        System.out.println("\nTesting read order:");
        Order retrievedOrder = orderDAO.read(newOrder.getOrderId());
        System.out.println("Retrieved order: " + retrievedOrder);
        System.out.println("Order items: " + retrievedOrder.getItems().size());
        
        // Test update order status
        System.out.println("\nTesting update order status:");
        boolean statusUpdated = orderDAO.updateOrderStatus(newOrder.getOrderId(), OrderStatus.PREPARING);
        System.out.println("Order status updated: " + statusUpdated);
        
        // Test get orders by customer
        System.out.println("\nTesting get orders by customer:");
        List<Order> customerOrders = orderDAO.getOrdersByUserId(customer.getUserId());
        System.out.println("Orders for customer: " + customerOrders.size());
        
        // Test get orders by status
        System.out.println("\nTesting get orders by status:");
        List<Order> processingOrders = orderDAO.getOrdersByStatus(OrderStatus.PREPARING);
        System.out.println("Processing orders: " + processingOrders.size());
        
        // Test get all orders
        System.out.println("\nTesting get all orders:");
        List<Order> allOrders = orderDAO.getAll();
        System.out.println("Total orders: " + allOrders.size());
        for (Order order : allOrders) {
            System.out.println(order);
        }
        
        // Test delete order
        System.out.println("\nTesting delete order:");
        boolean deleted = orderDAO.delete(newOrder.getOrderId());
        System.out.println("Order deleted: " + deleted);
        
        // Verify deletion
        Order deletedOrder = orderDAO.read(newOrder.getOrderId());
        System.out.println("Order after deletion: " + (deletedOrder == null ? "Not found" : deletedOrder));
        
    }
}