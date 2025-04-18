package manager;

import database.ProductsDAO;
import model.Products;
import java.util.List;

/**
 * Manager class for Product-related operations, interacting with ProductsDAO.
 */
public class ProductManager {

    private static ProductManager instance;
    private final ProductsDAO productsDAO;

    private ProductManager() {
        this.productsDAO = new ProductsDAO();
    }

    public static synchronized ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    /**
     * Gets a product by its ID.
     * @param productId The ID of the product.
     * @return The Products object or null if not found.
     */
    public Products getProductById(int productId) {
        return productsDAO.getProductById(productId);
    }

    /**
     * Gets all unique product categories.
     * @return List of all categories.
     */
    public List<String> getAllCategories() {
        return productsDAO.getAllCategories();
    }

    /**
     * Gets all products.
     * @return List of all products.
     */
    public List<Products> getAllProducts() {
        return productsDAO.getAllProducts();
    }
    
    public List<Products> getAllProducts(boolean is){
        return productsDAO.getAllProduct(true);
    }

    /**
     * Gets products by category.
     * @param category The category to filter by.
     * @return List of products in the specified category.
     */
    public List<Products> getProductsByCategory(String category) {
        return productsDAO.getProductsByCategory(category);
    }

    /**
     * Adds a new product.
     * @param product The Products object to add.
     * @return true if the product was added successfully, false otherwise.
     */
    public boolean addProduct(Products product) {
        return productsDAO.addProduct(product);
    }

    /**
     * Updates an existing product.
     * @param product The Products object to update.
     * @return true if the product was updated successfully, false otherwise.
     */
    public boolean updateProduct(Products product) {
        return productsDAO.update(product);
    }

    /**
     * Updates the stock quantity of a product.
     * @param productId The ID of the product.
     * @param newQuantity The new stock quantity.
     * @return true if the stock quantity was updated successfully, false otherwise.
     */
    public boolean updateStockQuantity(int productId, int newQuantity) {
        return productsDAO.updateStockQuantity(productId, newQuantity);
    }

    /**
     * Deletes a product by its ID.
     * @param productId The ID of the product to delete.
     * @return true if the product was deleted successfully, false otherwise.
     */
    public boolean deleteProduct(int productId) {
        return productsDAO.deleteProduct(productId);
    }

    /**
     * Searches products by name.
     * @param searchTerm The search term.
     * @return List of products matching the search term.
     */
    public List<Products> searchProductsByName(String searchTerm) {
        return productsDAO.searchProductsByName(searchTerm);
    }

    /**
     * Gets the best selling products.
     * @param limit Maximum number of products to return
     * @return List of best selling products.
     */
    public List<Products> getBestSellerProducts(int limit) {
        return productsDAO.getBestSellerProducts(limit);
    }

    /**
     * Gets the newest products (newcomers).
     * @param limit Maximum number of products to return
     * @return List of newest products.
     */
    public List<Products> getNewcomerProducts(int limit) {
        return productsDAO.getNewcomerProducts(limit);
    }
}