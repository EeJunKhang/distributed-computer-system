package database;

import model.Products;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Products entities
 */
public class ProductsDAO extends DBOperation<Products, Integer> {

    /**
     * Default constructor
     */
    public ProductsDAO() {
        super();
    }

    /**
     * Helper method to create a Products object from a ResultSet
     * @param rs The ResultSet
     * @return The Products object
     * @throws SQLException If a database error occurs
     */
    @Override
    protected Products mapResultSetToEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("product_id");
        String itemName = rs.getString("name");
        String itemDescription = rs.getString("description");
        double price = rs.getDouble("price");
        String category = rs.getString("category");
        String image = rs.getString("image_url");
        int stockQuantity = rs.getInt("stock_quantity");
        String lastUpdated = rs.getString("last_updated");
        
        return new Products(id, itemName, itemDescription, price, category, image, stockQuantity, lastUpdated);
    }
    
    @Override
    public boolean create(Products product) {
        String sql = "INSERT INTO products (name, description, price, category, image_url, stock_quantity, last_updated) " +
                        "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, product.getItemName());
                stmt.setString(2, product.getItemDescription());
                stmt.setDouble(3, product.getPrice());
                stmt.setString(4, product.getCategory());
                stmt.setString(5, product.getImage());
                stmt.setInt(6, product.getStockQuantity());
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows == 0) {
                    return false;
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
    }
    
    /**
     * Alternative method name for create - for backward compatibility
     * @param product The product to add
     * @return True if successful, false otherwise
     */
    public boolean addProduct(Products product) {
        return create(product);
    }

    @Override
    public Products read(Integer productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, productId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                    return null;
                }
            }
        });
    }
    
    /**
     * Alternative method name for read - for backward compatibility
     * @param productId The product ID
     * @return The product or null if not found
     */
    public Products getProductById(int productId) {
        return read(productId);
    }

    @Override
    public boolean update(Products product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, " +
                     "category = ?, image_url = ?, stock_quantity = ?, last_updated = NOW() " +
                     "WHERE product_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, product.getItemName());
                stmt.setString(2, product.getItemDescription());
                stmt.setDouble(3, product.getPrice());
                stmt.setString(4, product.getCategory());
                stmt.setString(5, product.getImage());
                stmt.setInt(6, product.getStockQuantity());
                stmt.setInt(7, product.getId());
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }
    
    /**
     * Update product stock quantity
     * @param productId The product ID
     * @param newQuantity The new stock quantity
     * @return True if successful, false otherwise
     */
    public boolean updateStockQuantity(int productId, int newQuantity) {
        String sql = "UPDATE products SET stock_quantity = ?, last_updated = NOW() WHERE product_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, newQuantity);
                stmt.setInt(2, productId);
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }

    @Override
    public boolean delete(Integer productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        
        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, productId);
                
                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            }
        });
    }
    
    /**
     * Alternative method name for delete - for backward compatibility
     * @param productId The product ID
     * @return True if successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        return delete(productId);
    }

    @Override
    public List<Products> getAll() {
        String sql = "SELECT * FROM products";
        
        return executeTransaction(conn -> {
            List<Products> products = new ArrayList<>();
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Products product = mapResultSetToEntity(rs);
                    products.add(product);
                }
                
                return products;
            }
        });
    }
    
    /**
     * Alternative method name for getAll - for backward compatibility
     * @return List of all products
     */
    public List<Products> getAllProducts() {
        return getAll();
    }
    
    /**
     * Get products by category
     * @param category The category to filter by
     * @return List of products in the specified category
     */
    public List<Products> getProductsByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ?";
        
        return executeTransaction(conn -> {
            List<Products> products = new ArrayList<>();
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, category);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Products product = mapResultSetToEntity(rs);
                        products.add(product);
                    }
                    
                    return products;
                }
            }
        });
    }
    
    /**
     * Search products by name
     * @param searchTerm The search term
     * @return List of products matching the search term
     */
    public List<Products> searchProductsByName(String searchTerm) {
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        
        return executeTransaction(conn -> {
            List<Products> products = new ArrayList<>();
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + searchTerm + "%");
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Products product = mapResultSetToEntity(rs);
                        products.add(product);
                    }
                    
                    return products;
                }
            }
        });
    }
}