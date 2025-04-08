package database.test;

import database.ProductsDAO;
import model.Products;
import java.util.List;

public class ProductsDAOTest {
    
    public static void main(String[] args) {
        testProductDAO();
    }
    
    public static void testProductDAO() {
        ProductsDAO productDAO = new ProductsDAO();
        
        // Test create
        System.out.println("Testing create product:");
        Products newProduct = new Products(
            "Test Product", 
            "This is a test product", 
            19.99, 
            "Test Category", 
            "test.jpg", 
            100
        );
        
        boolean created = productDAO.create(newProduct);
        System.out.println("Product created: " + created);
        System.out.println("Product ID: " + newProduct.getId());
        
        // Test read
        System.out.println("\nTesting read product:");
        Products retrievedProduct = productDAO.read(newProduct.getId());
        System.out.println("Retrieved product: " + retrievedProduct);
        
        // Test update
        System.out.println("\nTesting update product:");
        retrievedProduct.setPrice(24.99);
        retrievedProduct.setStockQuantity(150);
        boolean updated = productDAO.update(retrievedProduct);
        System.out.println("Product updated: " + updated);
        
        // Test get all products
        System.out.println("\nTesting get all products:");
        List<Products> allProducts = productDAO.getAll();
        System.out.println("Total products: " + allProducts.size());
        for (Products product : allProducts) {
            System.out.println(product);
        }
        
        // Test get products by category
        System.out.println("\nTesting get products by category:");
        List<Products> categoryProducts = productDAO.getProductsByCategory("Test Category");
        System.out.println("Products in Test Category: " + categoryProducts.size());
        
        // Test search products by name
        System.out.println("\nTesting search products by name:");
        List<Products> searchResults = productDAO.searchProductsByName("Test");
        System.out.println("Products with 'Test' in name: " + searchResults.size());
        
        // Test update stock quantity
        System.out.println("\nTesting update stock quantity:");
        boolean stockUpdated = productDAO.updateStockQuantity(newProduct.getId(), 75);
        System.out.println("Stock quantity updated: " + stockUpdated);
        
        // Test delete
        System.out.println("\nTesting delete product:");
        boolean deleted = productDAO.delete(newProduct.getId());
        System.out.println("Product deleted: " + deleted);
        
        // Verify deletion
        Products deletedProduct = productDAO.read(newProduct.getId());
        System.out.println("Product after deletion: " + (deletedProduct == null ? "Not found" : deletedProduct));
    }
}