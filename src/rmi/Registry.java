package rmi;

import rmi.server.ProductServer;
import rmi.server.UserServer;
import rmi.server.OrderServer;
import rmi.server.AuthServer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import security.RMISSLClientSocketFactory;
import security.RMISSLServerSocketFactory;
import utils.ConfigReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Registry {
    private static final Logger logger = Logger.getLogger(Registry.class.getName());
    private static String logFolderPath;

    public static void setupLogging() {
        try {
            File baseLogDir = new File("src/logs");
            if (!baseLogDir.exists()) {
                baseLogDir.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = dateFormat.format(new Date());
            
            logFolderPath = "src/logs/session_" + timestamp;
            File sessionLogDir = new File(logFolderPath);
            sessionLogDir.mkdir();
            
            FileHandler fileHandler = new FileHandler(logFolderPath + "/application.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            
            PrintStream outStream = new PrintStream(new FileOutputStream(logFolderPath + "/stdout.log"));
            PrintStream errStream = new PrintStream(new FileOutputStream(logFolderPath + "/stderr.log"));
            
            System.setOut(outStream);
            System.setErr(errStream);
            
            logger.log(Level.INFO, "Logging initialized successfully to {0}", logFolderPath);
        } catch (IOException e) {
            System.err.println("Failed to set up logging: " + e.getMessage());
        }
    }

    public static void main(String args[]) throws RemoteException {
        setupLogging();
        
        logger.info("Starting RMI Registry...");
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();
        
        logger.log(Level.INFO, "Server IP: {0}, RMI Port: {1}", new Object[]{serverIP, rmiPort});
        
        System.setProperty("java.rmi.server.hostname", serverIP);
        try {
            logger.info("Creating RMI Registry with SSL security...");
            java.rmi.registry.Registry reg = LocateRegistry.createRegistry(rmiPort, new RMISSLClientSocketFactory(),
                    new RMISSLServerSocketFactory());

            logger.info("Initializing service instances...");
            AuthServer authServer = new AuthServer();
            OrderServer orderServer = new OrderServer();
            UserServer userServer = new UserServer();
            ProductServer productServer = new ProductServer();

            logger.info("Binding services to registry...");
            reg.rebind("AuthService", authServer);
            reg.rebind("OrderService", orderServer);
            reg.rebind("UserService", userServer);
            reg.rebind("ProductService", productServer);
            
            logger.info("All services successfully bound to registry");
            
            try (PrintStream summaryStream = new PrintStream(new FileOutputStream(logFolderPath + "/summary.log"))) {
                String summaryMessage = """
                                        RMI Server started successfully
                                        Time: """ + new Date() + "\n" +
                                       "Server IP: " + serverIP + "\n" +
                                       "RMI Port: " + rmiPort + "\n" +
                                       "Services: AuthService, OrderService, UserService, ProductService";
                summaryStream.println(summaryMessage);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not write summary log: {0}", e.getMessage());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error starting RMI Registry: {0}", e.getMessage());
        }
        
        String successMessage = "Server is running on IP: " + serverIP + " \nServer is bound to port: " + rmiPort;
        System.out.println(successMessage);
        logger.info(successMessage);
        System.out.println("Logs are being saved to: " + logFolderPath);
    }
}