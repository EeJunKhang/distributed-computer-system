package database.test;

import database.OrderItemDAO;
import database.ProductsDAO;
import model.OrderItem;
import model.Products;

import java.util.List;

public class OrderItemDAOTest {
    
    public static void main(String[] args) {
        testOrderItemDAO();
    }
    
    public static void testOrderItemDAO() {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        ProductsDAO productsDAO = new ProductsDAO();
        
        // First, we need an existing order ID and product
        int orderId = 1; // Replace with a valid order ID from your database
        Products product = productsDAO.read(1); // Replace with a valid product ID
        
        if (product == null) {
            System.out.println("No product found with ID 1. Please use a valid product ID.");
            return;
        }
        
        // Test add order item
        System.out.println("Testing add order item:");
        OrderItem newItem = new OrderItem(product, 3); // 3 of the product
        boolean added = orderItemDAO.addOrderItem(orderId, newItem);
        System.out.println("Order item added: " + added);
        System.out.println("Order item ID: " + newItem.getOrderItemId());
        
        // Test read order item
        System.out.println("\nTesting read order item:");
        OrderItem retrievedItem = orderItemDAO.read(newItem.getOrderItemId());
        System.out.println("Retrieved item product: " + retrievedItem.getProduct().getItemName());
        System.out.println("Retrieved item quantity: " + retrievedItem.getQuantity());
        
        // Test update quantity
        System.out.println("\nTesting update order item quantity:");
        boolean quantityUpdated = orderItemDAO.updateQuantity(newItem.getOrderItemId(), 5);
        System.out.println("Quantity updated: " + quantityUpdated);
        
        // Test get items by order ID
        System.out.println("\nTesting get items by order ID:");
        List<OrderItem> orderItems = orderItemDAO.getItemsByOrderId(orderId);
        System.out.println("Items in order " + orderId + ": " + orderItems.size());
        for (OrderItem item : orderItems) {
            System.out.println(item.getProduct().getItemName() + " - Quantity: " + item.getQuantity());
        }
        
        // Test delete order item (optional - uncomment if you want to test deletion)
        /*
        System.out.println("\nTesting delete order item:");
        boolean deleted = orderItemDAO.delete(newItem.getOrderItemId());
        System.out.println("Order item deleted: " + deleted);
        
        // Verify deletion
        OrderItem deletedItem = orderItemDAO.read(newItem.getOrderItemId());
        System.out.println("Order item after deletion: " + (deletedItem == null ? "Not found" : deletedItem));
        */
    }
}