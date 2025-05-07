// RMI/OrderServer.java
package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import manager.AuthManager;
import manager.OrderManager;
import model.AuthToken;
import model.Order;
import model.User;
import enums.OrderStatus;
import enums.PaymentStatus;
import model.Payment;
import enums.UserRole;
import java.util.Map;
import java.util.Random;
import model.ReportData;
import rmi.OrderInterface;
import security.RMISSLClientSocketFactory;
import security.RMISSLServerSocketFactory;
import utils.ConfigReader;
import utils.IPIdentifier;

public class OrderServer extends UnicastRemoteObject implements OrderInterface {

    private final OrderManager orderManager;
    private final AuthManager authManager;

    public OrderServer() throws RemoteException, Exception {
        super(ConfigReader.getRmiPort(), new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
        this.orderManager = OrderManager.getInstance();
        this.authManager = AuthManager.getInstance();
    }

    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("Order Service called from IP: " + clientIP);
    }

    private User validateTokenAndGetUser(AuthToken token) {
        if (token == null) {
            return null;
        }
        return authManager.getUserByToken(token.getToken());
    }

    @Override
    public Order getOrderById(AuthToken token, int orderId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getOrderById from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getOrderById(orderId);
    }

    @Override
    public List<Order> getAllOrders(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for getAllOrders from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getAllOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(AuthToken token, int userId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getUserId() != userId) {
            System.out.println("Authentication failed or unauthorized for getOrdersByUserId from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getOrdersByUserId(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(AuthToken token, OrderStatus status) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for getOrdersByStatus from " + IPIdentifier.getClientIP());
            return null;
        }
        return orderManager.getOrdersByStatus(status);
    }

    @Override
    public boolean createOrder(AuthToken token, Order order, boolean isBankPayment, Map<String, String> paymentInfo) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getUserId() != order.getUser().getUserId()) {
            System.out.println("Authentication failed or unauthorized for createOrder from " + IPIdentifier.getClientIP());
            return false;
        }

        // Create a thread to handle the order processing with simulated delays
        OrderProcessingThread processor = new OrderProcessingThread(
                orderManager,
                order,
                isBankPayment,
                paymentInfo
        );
        processor.start();

        // Wait for the thread to complete and return its result
        try {
            processor.join();
            return processor.getResult();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Order processing was interrupted");
            return false;
        }
    }

    @Override
    public boolean updateOrderStatus(AuthToken token, int orderId, OrderStatus status) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for updateOrderStatus from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.updateOrderStatus(orderId, status);
    }

    @Override
    public boolean deleteOrder(AuthToken token, int orderId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for deleteOrder from " + IPIdentifier.getClientIP());
            return false;
        }
        return orderManager.deleteOrder(orderId);
    }

    @Override
    public ReportData getReportData(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != UserRole.ADMIN) {

            return null;
        }
        var orderList = orderManager.getAllOrders();
        double totalSales = 0;
        int totalItem = 0;
        int totalOrder = orderList.size();
        for (var i : orderList) {
            totalSales = totalSales + i.getTotalPrice();
            totalItem = totalItem + i.getItems().size();
        }

        double average = totalSales / totalOrder;
//        average = Math.round(average * 100.0) / 100.0;
        return new ReportData(totalSales, totalOrder, totalItem, average);
    }

    @Override
    public List<Payment> getAllPaymentData(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() != enums.UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for deleteOrder from " + IPIdentifier.getClientIP());
            return null;
        }

        return orderManager.getAllPayment();
    }

//    private String generateTransactionId() {
//        Random random = new Random();
//        // Generate 3 random digits (000-999)
//        int randomDigits = random.nextInt(1000);
//
//        // Format the transaction ID with leading zeros where needed
//        return String.format("trans%03d", randomDigits);
//    }
    // Inner class to handle the threaded order processing
    private static class OrderProcessingThread extends Thread {

        private final OrderManager orderManager;
        private final Order order;
        private final boolean isBankPayment;
        private final Map<String, String> paymentInfo;
        private boolean result = false;

        public OrderProcessingThread(OrderManager orderManager, Order order,
                boolean isBankPayment, Map<String, String> paymentInfo) {
            this.orderManager = orderManager;
            this.order = order;
            this.isBankPayment = isBankPayment;
            this.paymentInfo = paymentInfo;
        }

        public boolean getResult() {
            return result;
        }

        @Override
        public void run() {
            try {
                // Step 1: Create order (with delay)
                System.out.println("Processing order creation...");
                Integer orderId = orderManager.createOrder(order);

                if (orderId == null) {
                    result = false;
                    return;
                }

                // Step 2: Create payment (with delay)
                System.out.println("Processing payment creation...");
                Payment payment = new Payment(
                        orderId,
                        order.getTotalPrice(),
                        isBankPayment ? "Bank Transfer" : "Credit Card",
                        generateTransactionId(),
                        PaymentStatus.Pending,
                        paymentInfo
                );

                Integer paymentId = orderManager.createPayment(payment);
                Thread.sleep(1500); // Simulate processing time

                if (paymentId == null) {
                    result = false;
                    return;
                }

                // Step 3: Update payment status (with delay)
                System.out.println("Updating payment status...");
                boolean res1 = orderManager.updatePaymentStatus(paymentId, PaymentStatus.Completed);

                if (!res1) {
                    result = false;
                    return;
                }

                // Step 4: Update order status (with delay)
                System.out.println("Updating order status...");
                boolean res2 = orderManager.updateOrderStatus(orderId, OrderStatus.CONFIRMED);

                result = res2;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Order processing thread was interrupted");
                result = false;
            }
        }

        private String generateTransactionId() {
            Random random = new Random();
            return String.format("trans%03d", random.nextInt(1000));
        }
    }
}
