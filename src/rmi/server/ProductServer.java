package rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import manager.AuthManager;
import manager.ProductManager;
import model.AuthToken;
import model.Products;
import model.User;
import enums.UserRole;
import rmi.ProductInterface;
import security.RMISSLClientSocketFactory;
import security.RMISSLServerSocketFactory;
import utils.ConfigReader;
import utils.IPIdentifier;

public class ProductServer extends UnicastRemoteObject implements ProductInterface {

    private final ProductManager productManager;
    private final AuthManager authManager;

    public ProductServer() throws RemoteException, Exception {
        super(ConfigReader.getRmiPort(), new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
        this.productManager = ProductManager.getInstance();
        this.authManager = AuthManager.getInstance();
    }

    private void showClientIP() {
        String clientIP = IPIdentifier.getClientIP();
        System.out.println("Product Service called from IP: " + clientIP);
    }

    private User validateTokenAndGetUser(AuthToken token) {
        if (token == null) {
            System.out.println("stest123");
            return null;
        }
        return authManager.getUserByToken(token.getToken());
    }

    @Override
    public Products getProductById(AuthToken token, int productId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getProductById from " + IPIdentifier.getClientIP());
            return null;
        }
        return productManager.getProductById(productId);
    }

    @Override
    public List<String> getAllCategories(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getAllCategories from " + IPIdentifier.getClientIP());
            return null;
        }
        return productManager.getAllCategories();
    }

    @Override
    public List<Products> getAllProducts(AuthToken token) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed or unauthorized for getAllProducts");
            return null;
        }
        List<Products> products = productManager.getAllProducts();
        return products;
    }

    @Override
    public List<Products> getAllProductsForView(AuthToken token) throws RemoteException {
        showClientIP();
//        System.out.println("getAllProducts called with token: " + (token != null ? token.getToken() : "null"));
        User tokenUser = validateTokenAndGetUser(token);
//        System.out.println("Token user: " + (tokenUser != null ? tokenUser.getUsername() : "null"));
        if (tokenUser == null) {
            System.out.println("Authentication failed or unauthorized for getAllProducts");
            return null;
        }
        List<Products> products = productManager.getAllProducts(true);
//        System.out.println("Returning products: " + (products == null ? "null" : products.size() + " items"));
        return products;
    }

    @Override
    public List<Products> getProductsByCategory(AuthToken token, String category) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getProductsByCategory from " + IPIdentifier.getClientIP());
            return null;
        }
        return productManager.getProductsByCategory(category);
    }

    @Override
    public boolean addProduct(AuthToken token, Products product) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed or unauthorized for addProduct from " + IPIdentifier.getClientIP());
            return false;
        }
        return productManager.addProduct(product);
    }

    @Override
    public boolean updateProduct(AuthToken token, Products product) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed or unauthorized for updateProduct from " + IPIdentifier.getClientIP());
            return false;
        }
        return productManager.updateProduct(product);
    }

    @Override
    public boolean updateStockQuantity(AuthToken token, int productId, int newQuantity) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null || tokenUser.getRole() == UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for updateStockQuantity from " + IPIdentifier.getClientIP());
            return false;
        }
        return productManager.updateStockQuantity(productId, newQuantity);
    }

    @Override
    public boolean deleteProduct(AuthToken token, int productId) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);

        if (tokenUser == null || tokenUser.getRole() !=  UserRole.ADMIN) {
            System.out.println("Authentication failed or unauthorized for deleteProduct from " + IPIdentifier.getClientIP());
            return false;
        }
        return productManager.deleteProduct(productId);
    }

    @Override
    public List<Products> searchProductsByName(AuthToken token, String searchTerm) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for searchProductsByName from " + IPIdentifier.getClientIP());
            return null;
        }
        return productManager.searchProductsByName(searchTerm);
    }

    @Override
    public List<Products> getBestSellerProducts(AuthToken token, int limit) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getBestSellerProducts from " + IPIdentifier.getClientIP());
            return null;
        }
        return productManager.getBestSellerProducts(limit);
    }

    @Override
    public List<Products> getNewcomerProducts(AuthToken token, int limit) throws RemoteException {
        showClientIP();
        User tokenUser = validateTokenAndGetUser(token);
        if (tokenUser == null) {
            System.out.println("Authentication failed for getNewcomerProducts from " + IPIdentifier.getClientIP());
            return null;
        }
        return productManager.getNewcomerProducts(limit);
    }
}
