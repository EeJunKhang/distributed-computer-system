package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.AuthToken;
import model.Products;

public interface ProductInterface extends Remote {

    /**
     * Gets a product by its ID.
     * @param token Authentication token.
     * @param productId The ID of the product.
     * @return The Products object or null if not found or unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    Products getProductById(AuthToken token, int productId) throws RemoteException;

    /**
     * Gets all products (admin only).
     * @param token Authentication token.
     * @return List of all products or null if unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    List<Products> getAllProducts(AuthToken token) throws RemoteException;

    /**
     * Gets products by category.
     * @param token Authentication token.
     * @param category The category to filter by.
     * @return List of products in the specified category or null if unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    List<Products> getProductsByCategory(AuthToken token, String category) throws RemoteException;

    /**
     * Adds a new product (admin only).
     * @param token Authentication token.
     * @param product The Products object to add.
     * @return true if the product was added successfully, false otherwise (unauthorized or database error).
     * @throws RemoteException If a remote error occurs.
     */
    boolean addProduct(AuthToken token, Products product) throws RemoteException;

    /**
     * Updates an existing product (admin only).
     * @param token Authentication token.
     * @param product The Products object to update.
     * @return true if the product was updated successfully, false otherwise (unauthorized or database error).
     * @throws RemoteException If a remote error occurs.
     */
    boolean updateProduct(AuthToken token, Products product) throws RemoteException;

    /**
     * Updates the stock quantity of a product (admin only).
     * @param token Authentication token.
     * @param productId The ID of the product to update.
     * @param newQuantity The new stock quantity.
     * @return true if the stock quantity was updated successfully, false otherwise (unauthorized or database error).
     * @throws RemoteException If a remote error occurs.
     */
    boolean updateStockQuantity(AuthToken token, int productId, int newQuantity) throws RemoteException;

    /**
     * Deletes a product by its ID (admin only).
     * @param token Authentication token.
     * @param productId The ID of the product to delete.
     * @return true if the product was deleted successfully, false otherwise (unauthorized or database error).
     * @throws RemoteException If a remote error occurs.
     */
    boolean deleteProduct(AuthToken token, int productId) throws RemoteException;

    /**
     * Searches products by name.
     * @param token Authentication token.
     * @param searchTerm The search term.
     * @return List of products matching the search term or null if unauthorized.
     * @throws RemoteException If a remote error occurs.
     */
    List<Products> searchProductsByName(AuthToken token, String searchTerm) throws RemoteException;
}