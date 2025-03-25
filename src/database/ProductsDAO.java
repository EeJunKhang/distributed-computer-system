/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import model.Products;

import java.sql.*;
import java.util.List;

public class ProductsDAO extends DBOperation<Products> {

    public ProductsDAO() {
        // Table "items" and its respective columns
        super("Products", "name", "description", "price", 
              "category", "image_url", "stock_quantity", "last_updated");
    }

    @Override
    protected Products mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Products(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDouble("price"),
            rs.getString("category"),
            rs.getString("image_url"),
            rs.getInt("stock_quantity"),
            rs.getString("last_updated")
        );
    }

    public void addProduct(Products product) throws SQLException {
        insert(product, product.getItemName(), product.getItemDescription(), product.getPrice(), 
               product.getCategory(), product.getImage(), product.getStockQuantity(), product.getLastUpdated());
    }

    public List<Products> getAllProducts() throws SQLException {
        return readAll();  
    }
    
    public Products getProductById(int productId) throws SQLException {
        List<Products> products = searchByColumn("product_id", productId, false);
        return products.isEmpty() ? null : products.get(0);
    }

    public void updateProduct(int itemId, String itemName, String itemDescription, double price, 
                           String category, String image, int stockQuantity, String lastUpdated) throws SQLException {
        update("products",itemId, itemName, itemDescription, price, category, image, stockQuantity, lastUpdated);
    }

    // Method to delete an item by its ID
    public void deleteProducts(int itemId, String column_name) throws SQLException {
        deleteById(itemId,column_name);  
    }
}

