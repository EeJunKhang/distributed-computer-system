/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package manager.testclient;

import rmi.OrderInterface;
import utils.TokenStorage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Customer;
import model.Order;
import model.OrderItem;
import model.AuthToken;
import model.Products;
import enums.OrderStatus;
import utils.ConfigReader;

public class OrderClient {

    private static OrderInterface orderService;
    private static AuthToken authToken = null;
    private static int loggedInUserId = 0; // Simulate logged-in user ID

    public static void main(String[] args) {
        try {
            connectToServer();

            // Try to load saved token
            authToken = TokenStorage.loadToken();
            if (authToken != null) {
                System.out.println("Found saved authentication session");
                System.out.println("Using existing session token: " + authToken.getToken());
                // should take from token
                // For testing simulate a logged-in user ID
                loggedInUserId = 1;
            } else {
                System.out.println("No saved session found. Please run the AuthClient first to login.");
                System.out.println("Some functionalities might be limited.");
            }

            runConsoleMenu();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void connectToServer() throws RemoteException, NotBoundException {
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();

        System.out.println("Connecting to server at " + serverIP + ":" + rmiPort);

        Registry registry = LocateRegistry.getRegistry(serverIP, rmiPort);
        orderService = (OrderInterface) registry.lookup("OrderService");

        System.out.println("Connected to order service");
    }

    private static void runConsoleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Order Management Client ===");
            if (authToken != null) {
                System.out.println("Status: Using Authentication Token (User ID: " + loggedInUserId + ")");
            } else {
                System.out.println("Status: No Authentication Token Available");
                System.out.println("Please run AuthClient first to login");
            }

            System.out.println("1. Get Order by ID");
            System.out.println("2. Get My Orders");
            System.out.println("3. Get All Orders (Admin)");
            System.out.println("4. Get Orders by Status");
            System.out.println("5. Create New Order");
            System.out.println("6. Update Order Status (Admin)");
            System.out.println("7. Delete Order (Admin)");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> getOrderById(scanner);
                    case "2" -> getMyOrders();
                    case "3" -> getAllOrders();
                    case "4" -> getOrdersByStatus(scanner);
                    case "5" -> createNewOrder(scanner);
                    case "6" -> updateOrderStatus(scanner);
                    case "7" -> deleteOrder(scanner);
                    case "8" -> exit = true;
                    default -> System.out.println("Invalid option!");
                }
            } catch (RemoteException e) {
                System.err.println("Error communicating with server: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void getOrderById(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());

        Order order = orderService.getOrderById(authToken, orderId);

        if (order != null) {
            displayOrderInfo(order);
        } else {
            System.out.println("Order not found or you don't have permission to view this order.");
        }
    }

    private static void getMyOrders() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        List<Order> orders = orderService.getOrdersByUserId(authToken, loggedInUserId);

        if (orders != null && !orders.isEmpty()) {
            System.out.println("\n=== My Orders ===");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId() +
                                    " | Date: " + order.getOrderTime() +
                                    " | Status: " + order.getStatus() +
                                    " | Total: $" + order.getTotalPrice());
            }
            System.out.println("Total orders: " + orders.size());
        } else {
            System.out.println("No orders found for user ID " + loggedInUserId + " or you don't have permission to view your orders.");
        }
    }

    private static void getAllOrders() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        List<Order> orders = orderService.getAllOrders(authToken);

        if (orders != null && !orders.isEmpty()) {
            System.out.println("\n=== All Orders ===");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId() +
                                    " | User: " + (order.getUser() != null ? order.getUser().getUsername() : "N/A") +
                                    " | Date: " + order.getOrderTime() +
                                    " | Status: " + order.getStatus() +
                                    " | Total: $" + order.getTotalPrice());
            }
            System.out.println("Total orders: " + orders.size());
        } else {
            System.out.println("No orders found or you don't have permission to view all orders (Admin only).");
        }
    }

    private static void getOrdersByStatus(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.println("Available statuses: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED");
        System.out.print("Enter order status: ");
        String statusStr = scanner.nextLine().toUpperCase();

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter a valid status.");
            return;
        }

        // NOT IMPMLEMENTED IN OrderInterface and OrderServer.
        // For now, this won't work.
        System.out.println("Error: getOrdersByStatus functionality is not implemented on the server.");
    }

    private static void createNewOrder(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        Customer customer = new Customer();
        customer.setUserId(loggedInUserId);

        List<OrderItem> items = new ArrayList<>();
        // Hardcode now
        Products product1 = new Products();
        product1.setId(1); 
        product1.setPrice(10.0);
        product1.setItemName("Product 1");
        items.add(new OrderItem(0, product1, 2));

        Products product2 = new Products();
        product2.setId(2); 
        product2.setPrice(25.50);
        product2.setItemName("Product 2");
        items.add(new OrderItem(0, product2, 1));

        Order newOrder = new Order(0, customer, null, OrderStatus.PENDING, 0.0, items);
        double totalPrice = 0;
        for (OrderItem item : newOrder.getItems()) {
            totalPrice += item.getQuantity() * item.getProduct().getPrice();
        }
        newOrder.setTotalPrice(totalPrice);

        boolean result = orderService.createOrder(authToken, newOrder);

        if (result) {
            System.out.println("New order created successfully with ID: " + newOrder.getOrderId());
        } else {
            System.out.println("Failed to create new order.");
        }
    }

    private static void updateOrderStatus(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter order ID to update: ");
        int orderId = Integer.parseInt(scanner.nextLine());

        System.out.println("Available statuses: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED");
        System.out.print("Enter new status: ");
        String statusStr = scanner.nextLine().toUpperCase();

        OrderStatus status;
        try {
            status = OrderStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter a valid status.");
            return;
        }

        boolean result = orderService.updateOrderStatus(authToken, orderId, status);

        if (result) {
            System.out.println("Order " + orderId + " status updated successfully to " + status);
        } else {
            System.out.println("Failed to update order status for order " + orderId + ". Check permissions (Admin only) and order ID.");
        }
    }

    private static void deleteOrder(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter order ID to delete: ");
        int orderId = Integer.parseInt(scanner.nextLine());

        boolean result = orderService.deleteOrder(authToken, orderId);

        if (result) {
            System.out.println("Order " + orderId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete order " + orderId + ". Check permissions (Admin only) and order ID.");
        }
    }

    private static void displayOrderInfo(Order order) {
        System.out.println("\n=== Order Details ===");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("User: " + (order.getUser() != null ? order.getUser().getUsername() : "N/A"));
        System.out.println("Date: " + order.getOrderTime());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Total: $" + order.getTotalPrice());

        System.out.println("\nOrder Items:");
        for (OrderItem item : order.getItems()) {
            System.out.println("- " + item.getProduct().getItemName() +
                                " (ID: " + item.getProduct().getId() + ") | " +
                                "Quantity: " + item.getQuantity() + " | " +
                                "Price: $" + item.getProduct().getPrice() + " each | " +
                               "Subtotal: $" + (item.getQuantity() * item.getProduct().getPrice()));
        }
    }
}