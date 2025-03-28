
package database;

import enums.OrderStatus;
import enums.UserRole;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.Admin;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.User;
import model.Products;


public class test {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) throws SQLException {
        OrderDAO orderDAO = new OrderDAO();

        try {
            
             int orderId = 15; // Example order ID
            Order order = orderDAO.searchByColumn("order_id", orderId,false).get(0); 

            if (order != null) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("User: " + order.getUser().getUsername());
                System.out.println("Order Time: " + order.getOrderTime());
                System.out.println("Status: " + order.getStatus());
                System.out.println("Total Price: $" + order.getTotalPrice());

                System.out.println("Order Items:");
                for (OrderItem item : order.getItems()) {
                    System.out.println("- " + item.getProduct().getItemName() + 
                                       " | Quantity: " + item.getQuantity());
                }
            } else {
                System.out.println("Order not found.");
            }
//            int userId = 1;  
//            double totalPrice = 50.75;
//            OrderStatus status = OrderStatus.PENDING;
//
//            orderDAO.addOrder(userId, status,totalPrice);
//
//            System.out.println("\nFetching Orders for User ID: " + userId);
//            List<Order> orders = orderDAO.getOrdersByUserId(userId);
//            for (Order order : orders) {
//                System.out.println("Order ID: " + order.getOrderId());
//                System.out.println("User: " + order.getUser().getUsername());
//                System.out.println("Status: " + order.getStatus());
//                System.out.println("Total Price: $" + order.getTotalPrice());
//                System.out.println("-----------");
//            }
//            
//            
//
//            if (!orders.isEmpty()) {
//                int orderId = orders.get(0).getOrderId();
//                OrderStatus newStatus = OrderStatus.CONFIRMED;
//
//                System.out.println("\nUpdating Order ID: " + orderId + " to " + newStatus);
//                orderDAO.updateOrder(orderId,2, newStatus,52);
//                System.out.println("Order Status Updated Successfully!");
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
//********************order item
//       OrderItemDAO orderItemDAO = new OrderItemDAO();
//        try {
//            // 1. Add a new order item
//            int orderId = 1;   
//            int productId = 4;  
//            int quantity = 3;
//            double pricePerUnit = 10.50;
//
//            orderItemDAO.addOrderItem(orderId, productId, quantity, pricePerUnit);
//
//            List<OrderItem> orderItems = orderItemDAO.getItemsByOrderId(orderId);
//            for (OrderItem item : orderItems) {
//                System.out.println("Order Item ID: " + item.getOrderItemId());
//                System.out.println("Product: " + item.getProduct().getItemName());
//                System.out.println("Quantity: " + item.getQuantity());
//                System.out.println("Price per Unit: $" + item.getPricePerUnit());
//                System.out.println("-----------");
//            }
//
//            // 3. Update an order item
//            if (!orderItems.isEmpty()) {
//                int orderItemId = orderItems.get(0).getOrderItemId();
//                int newQuantity = 5;
//                double newPricePerUnit = 9.99;
//
//                orderItemDAO.updateOrderItem(orderItemId,2,5, newQuantity, newPricePerUnit);
//            }
//
//            // 4. Delete an order item
//            if (!orderItems.isEmpty()) {
//                int orderItemId = orderItems.get(0).getOrderItemId();
//                orderItemDAO.deleteOrderItem(orderItemId);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//*************************USER********************* 
//         try {
//            UserDAO userDAO = new UserDAO();
//
//            userDAO.createUser("John", "Doe", "huhuuhuuuhuh", "hashed_password", 
//                               "john@example.com", "123 Main St", "1234567890", UserRole.CUSTOMER);
//
//            userDAO.updateUser(1, "John", "Doe", "hahaha", "new_hashed_password", 
//                               "john_new@example.com", "456 Main St", "0987654321", UserRole.ADMIN);
//            System.out.println("User updated successfully!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//*************************ITEM********************* 
//       try {
//            ItemDAO itemDao = new ItemDAO();
//            // 1. Add a new item
////            Products newItem = new Products(0, "Smartphone", "Latest model with features", 699.99,
       ////                                      "Electronics", "smartphone.jpg", 100, "2025-03-25");
       ////            itemDao.addItem(newItem);
//
//            // 2. Get all items
//            List<Products> items = itemDao.getAllItems();
//            for (Products item : items) {
//                System.out.println("Item ID: " + item.getId() + ", Name: " + item.getItemName());
//            }
//
//            // 3. Update an item
////            itemDao.updateItem(1, "Smartphone", "Updated description", 749.99, "Electronics",
       ////                               "smartphone_updated.jpg", 90, "2025-03-26");
//
//            // 4. Delete an item
//            itemDao.deleteItem(2, "product_id");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        ProductsDAO productsDAO = new ProductsDAO();
//        
//        try {
//            int productId = 1; 
//            Products product = productsDAO.getProductById(productId);
//
//            if (product != null) {
//                System.out.println("Product ID: " + product.getId());
//                System.out.println("Name: " + product.getItemName());
//                System.out.println("Description: " + product.getItemDescription());
//                System.out.println("Price: $" + product.getPrice());
//                System.out.println("Category: " + product.getCategory());
//                System.out.println("Image URL: " + product.getImage());
//                System.out.println("Stock Quantity: " + product.getStockQuantity());
//                System.out.println("Last Updated: " + product.getLastUpdated());
//            } else {
//                System.out.println("Product not found!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        } 
   }
}
