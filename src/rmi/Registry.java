package rmi;

import rmi.server.ProductServer;
import rmi.server.UserServer;
import rmi.server.OrderServer;
import rmi.server.AuthServer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

//import security.RMISSLServerSocketFactory;
import utils.ConfigReader;

public class Registry {

    public static void main(String args[]) throws RemoteException {
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();

        System.setProperty("java.rmi.server.hostname", serverIP);

        java.rmi.registry.Registry reg = LocateRegistry.createRegistry(rmiPort);

        // Create server instances
        AuthServer authServer = new AuthServer();
        OrderServer orderServer = new OrderServer();
        UserServer userServer = new UserServer();
        ProductServer productServer = new ProductServer();

        // int sslPort = 1041;
        // RMISSLServerSocketFactory ssf = new RMISSLServerSocketFactory();
        // // associating SSL socket factory
        // AuthServer authServer = (AuthServer) UnicastRemoteObject.exportObject(
        //     new AuthServer(), sslPort, null, ssf);
        // OrderServer orderServer = (OrderServer) UnicastRemoteObject.exportObject(n
        // ew OrderServer(), sslPort, null, ssf);
        // UserServer userServer = (UserServer) UnicastRemoteObject.exportObject(
        //     new UserServer(), sslPort, null, ssf);
        // ProductServer productServer = (ProductServer) UnicastRemoteObject.exportObject(
        //     new ProductServer(), sslPort, null, ssf);
        // DashboardServer dashboardServer = (DashboardServer) UnicastRemoteObject.exportObject(
        //     new DashboardServer(), sslPort, null, ssf);
        // Authentication services
        reg.rebind("AuthService", authServer);
//        reg.rebind("handleLogin", authServer);
//        reg.rebind("handleLogout", authServer);
//        reg.rebind("verifyToken", authServer);
//        reg.rebind("handleRegister", authServer);

        // Order management services
        reg.rebind("OrderService", orderServer);
        // reg.rebind("getOrderById", orderServer); // Redundent?
        // reg.rebind("getAllOrders", orderServer);
        // reg.rebind("getOrdersByUserId", orderServer);
        // reg.rebind("createOrder", orderServer);
        // reg.rebind("updateOrderStatus", orderServer);
        // reg.rebind("deleteOrder", orderServer);

        // User management services
        reg.rebind("UserService", userServer);
        // reg.rebind("getUserById", userServer); // Redundent?
        // reg.rebind("getUserByUsername", userServer);
        // reg.rebind("getAllUsers", userServer);
        // reg.rebind("getAllCustomers", userServer);
        // reg.rebind("getAllAdmins", userServer);
        // reg.rebind("updateUserProfile", userServer);
        // reg.rebind("changePassword", userServer);
        // reg.rebind("deleteUser", userServer);
        // reg.rebind("promoteToAdmin", userServer);

        // Product management services
        reg.rebind("ProductService", productServer);

        // System.out.println("SSL RMI Server is running on IP: " + serverIP
        // + " and SSL port: " + sslPort);
        // System.out.println("RMI Registry is running on port: " + rmiPort);
        System.out.println("Server is running on IP: " + serverIP
                + " \nServer is bound to port: " + rmiPort);
    }
}
