/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import model.Items;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ItemDAO {
    public void addItem(Items items) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, category, image_url, stock_quantity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, items.getItemName());
            stmt.setString(2, items.getItemDescription());
            stmt.setDouble(3, items.getPrice());
            stmt.setString(4, items.getCategory());
            stmt.setString(5, items.getImage());
            stmt.setInt(6, items.getStockQuantity());

            
            stmt.executeUpdate();
        }
    }

    public Items getProductById(int item) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, item);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Items(
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
        }
        return null;
    }


    public List<Items> getAllProducts() throws SQLException {
        List<Items> productList = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Items product = new Items(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getString("image_url"),
                        rs.getInt("stock_quantity"),
                        rs.getString("last_updated")
                );
                productList.add(product);
            }
        }
        return productList;
    }

    public void updateProduct(Items item) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?,"
                + " price = ?, category = ?, image_url = ?,"
                + " stock_quantity = ? WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemDescription());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getCategory());
            stmt.setString(5, item.getImage());
            stmt.setInt(6, item.getStockQuantity());
            stmt.setInt(7, item.getId()); 

            stmt.executeUpdate();
        }
    }

    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
