package manager.testclient;

import rmi.ProductInterface;
import utils.TokenStorage;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import model.Products;
import model.AuthToken;
import utils.ConfigReader;

public class ProductClient {

    private static ProductInterface productService;
    private static AuthToken authToken = null;
    private static int loggedInUserId = 0; // Simulate logged-in user ID

    public static void main(String[] args) {
        try {
            connectToServer();

            // Try to load saved token
            authToken = TokenStorage.loadToken();
            if (authToken != null) {
                System.out.println("Found saved authentication session");
                System.out.println("Using existing session token: " + authToken.getToken());
                // should take from token
                // For testing simulate a logged-in user ID
                loggedInUserId = 1;
            } else {
                System.out.println("No saved session found. Please run the AuthClient first to login.");
                System.out.println("Some functionalities might be limited.");
            }

            runConsoleMenu();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void connectToServer() throws RemoteException, NotBoundException {
        String serverIP = ConfigReader.getServerIP();
        int rmiPort = ConfigReader.getRmiPort();

        System.out.println("Connecting to server at " + serverIP + ":" + rmiPort);

        Registry registry = LocateRegistry.getRegistry(serverIP, rmiPort);
        productService = (ProductInterface) registry.lookup("ProductService");

        System.out.println("Connected to product service");
    }

    private static void runConsoleMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n=== Product Management Client ===");
            if (authToken != null) {
                System.out.println("Status: Using Authentication Token (User ID: " + loggedInUserId + ")");
            } else {
                System.out.println("Status: No Authentication Token Available");
                System.out.println("Please run AuthClient first to login");
            }

            System.out.println("1. Get Product by ID");
            System.out.println("2. Get All Products (Admin)");
            System.out.println("3. Get Products by Category");
            System.out.println("4. Add New Product (Admin)");
            System.out.println("5. Update Product (Admin)");
            System.out.println("6. Update Stock Quantity (Admin)");
            System.out.println("7. Delete Product (Admin)");
            System.out.println("8. Search Products by Name");
            System.out.println("9. Get All Categories");
            System.out.println("10. View Best Seller Products");
            System.out.println("11. View Newcomer Products");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> getProductById(scanner);
                    case "2" -> getAllProducts();
                    case "3" -> getProductsByCategory(scanner);
                    case "4" -> addNewProduct(scanner);
                    case "5" -> updateProduct(scanner);
                    case "6" -> updateStockQuantity(scanner);
                    case "7" -> deleteProduct(scanner);
                    case "8" -> searchProductsByName(scanner);
                    case "9" -> getAllCategories();
                    case "10" -> getBestSellerProducts(scanner);
                    case "11" -> getNewcomerProducts(scanner);
                    case "0" -> exit = true;
                    default -> System.out.println("Invalid option!");
                }
            } catch (RemoteException e) {
                System.err.println("Error communicating with server: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void getProductById(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter product ID: ");
        int productId = Integer.parseInt(scanner.nextLine());

        Products product = productService.getProductById(authToken, productId);

        if (product != null) {
            displayProductInfo(product);
        } else {
            System.out.println("Product not found or you don't have permission to view this product.");
        }
    }

    private static void getAllProducts() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        List<Products> products = productService.getAllProducts(authToken);

        if (products != null && !products.isEmpty()) {
            System.out.println("\n=== All Products ===");
            for (Products product : products) {
                System.out.println(product.getId() + ": " + product.getItemName() + 
                                   " | Price: $" + product.getPrice() + 
                                   " | Stock: " + product.getStockQuantity());
            }
            System.out.println("Total products: " + products.size());
        } else {
            System.out.println("No products found or you don't have permission to view all products (Admin only).");
        }
    }

    private static void getProductsByCategory(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        List<Products> products = productService.getProductsByCategory(authToken, category);

        if (products != null && !products.isEmpty()) {
            System.out.println("\n=== Products in category: " + category + " ===");
            for (Products product : products) {
                System.out.println(product.getId() + ": " + product.getItemName() + 
                                   " | Price: $" + product.getPrice() + 
                                   " | Stock: " + product.getStockQuantity());
            }
            System.out.println("Total products in category: " + products.size());
        } else {
            System.out.println("No products found in category \"" + category + "\" or you don't have permission.");
        }
    }

    private static void addNewProduct(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        Products newProduct = new Products();
        
        System.out.println("\n=== Add New Product ===");
        System.out.print("Name: ");
        newProduct.setItemName(scanner.nextLine());
        
        System.out.print("Description: ");
        newProduct.setItemDescription(scanner.nextLine());
        
        System.out.print("Price: ");
        newProduct.setPrice(Double.parseDouble(scanner.nextLine()));
        
        System.out.print("Category: ");
        newProduct.setCategory(scanner.nextLine());
        
        System.out.print("Image URL: ");
        newProduct.setImageUrl(scanner.nextLine());
        
        System.out.print("Stock Quantity: ");
        newProduct.setStockQuantity(Integer.parseInt(scanner.nextLine()));
        
        boolean result = productService.addProduct(authToken, newProduct);
        
        if (result) {
            System.out.println("Product added successfully with ID: " + newProduct.getId());
        } else {
            System.out.println("Failed to add product. Check permissions (Admin only).");
        }
    }

    private static void updateProduct(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter product ID to update: ");
        int productId = Integer.parseInt(scanner.nextLine());
        
        Products product = productService.getProductById(authToken, productId);
        
        if (product == null) {
            System.out.println("Product not found or you don't have permission to view this product.");
            return;
        }
        
        System.out.println("\n=== Update Product ===");
        System.out.println("Current product details:");
        displayProductInfo(product);
        
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("Name [" + product.getItemName() + "]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            product.setItemName(input);
        }
        
        System.out.print("Description [" + product.getItemDescription() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            product.setItemDescription(input);
        }
        
        System.out.print("Price [" + product.getPrice() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            product.setPrice(Double.parseDouble(input));
        }
        
        System.out.print("Category [" + product.getCategory() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            product.setCategory(input);
        }
        
        System.out.print("Image URL [" + product.getImage() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            product.setImageUrl(input);
        }
        
        System.out.print("Stock Quantity [" + product.getStockQuantity() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            product.setStockQuantity(Integer.parseInt(input));
        }
        
        boolean result = productService.updateProduct(authToken, product);
        
        if (result) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Failed to update product. Check permissions (Admin only).");
        }
    }

    private static void updateStockQuantity(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter product ID: ");
        int productId = Integer.parseInt(scanner.nextLine());
        
        Products product = productService.getProductById(authToken, productId);
        
        if (product == null) {
            System.out.println("Product not found or you don't have permission to view this product.");
            return;
        }
        
        System.out.println("Current stock quantity for " + product.getItemName() + ": " + product.getStockQuantity());
        System.out.print("Enter new stock quantity: ");
        int newQuantity = Integer.parseInt(scanner.nextLine());
        
        boolean result = productService.updateStockQuantity(authToken, productId, newQuantity);
        
        if (result) {
            System.out.println("Stock quantity updated successfully.");
        } else {
            System.out.println("Failed to update stock quantity. Check permissions (Admin only).");
        }
    }

    private static void deleteProduct(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter product ID to delete: ");
        int productId = Integer.parseInt(scanner.nextLine());
        
        Products product = productService.getProductById(authToken, productId);
        
        if (product == null) {
            System.out.println("Product not found or you don't have permission to view this product.");
            return;
        }
        
        System.out.println("You are about to delete the following product:");
        displayProductInfo(product);
        
        System.out.print("Are you sure you want to delete this product? (y/n): ");
        String confirmation = scanner.nextLine().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            boolean result = productService.deleteProduct(authToken, productId);
            
            if (result) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Failed to delete product. Check permissions (Admin only).");
            }
        } else {
            System.out.println("Product deletion cancelled.");
        }
    }

    private static void searchProductsByName(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }

        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();
        
        List<Products> products = productService.searchProductsByName(authToken, searchTerm);
        
        if (products != null && !products.isEmpty()) {
            System.out.println("\n=== Search Results for \"" + searchTerm + "\" ===");
            for (Products product : products) {
                System.out.println(product.getId() + ": " + product.getItemName() + 
                                   " | Price: $" + product.getPrice() + 
                                   " | Stock: " + product.getStockQuantity());
            }
            System.out.println("Total matching products: " + products.size());
        } else {
            System.out.println("No products found matching \"" + searchTerm + "\" or you don't have permission.");
        }
    }

    private static void getAllCategories() throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
    
        List<String> categories = productService.getAllCategories(authToken);
        
        if (categories != null && !categories.isEmpty()) {
            System.out.println("\n=== All Product Categories ===");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }
            System.out.println("Total categories: " + categories.size());
        } else {
            System.out.println("No categories found or you don't have permission.");
        }
    }

    private static void getBestSellerProducts(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
    
        System.out.print("Enter maximum number of products to display: ");
        int limit = Integer.parseInt(scanner.nextLine());
        
        List<Products> products = productService.getBestSellerProducts(authToken, limit);
        
        if (products != null && !products.isEmpty()) {
            System.out.println("\n=== Best Seller Products ===");
            for (Products product : products) {
                System.out.println(product.getId() + ": " + product.getItemName() + 
                                    " | Price: $" + product.getPrice() + 
                                    " | Stock: " + product.getStockQuantity());
            }
            System.out.println("Total best seller products displayed: " + products.size());
        } else {
            System.out.println("No best seller products found or you don't have permission.");
        }
    }
    
    private static void getNewcomerProducts(Scanner scanner) throws RemoteException {
        if (authToken == null) {
            System.out.println("No authentication token available!");
            return;
        }
    
        System.out.print("Enter maximum number of products to display: ");
        int limit = Integer.parseInt(scanner.nextLine());
        
        List<Products> products = productService.getNewcomerProducts(authToken, limit);
        
        if (products != null && !products.isEmpty()) {
            System.out.println("\n=== Newcomer Products ===");
            for (Products product : products) {
                System.out.println(product.getId() + ": " + product.getItemName() + 
                                    " | Price: $" + product.getPrice() + 
                                    " | Stock: " + product.getStockQuantity() +
                                    " | Added: " + product.getLastUpdated());
            }
            System.out.println("Total newcomer products displayed: " + products.size());
        } else {
            System.out.println("No newcomer products found or you don't have permission.");
        }
    }

    private static void displayProductInfo(Products product) {
        System.out.println("\n=== Product Details ===");
        System.out.println("ID: " + product.getId());
        System.out.println("Name: " + product.getItemName());
        System.out.println("Description: " + product.getItemDescription());
        System.out.println("Price: $" + product.getPrice());
        System.out.println("Category: " + product.getCategory());
        System.out.println("Image URL: " + product.getImage());
        System.out.println("Stock Quantity: " + product.getStockQuantity());
        System.out.println("Last Updated: " + product.getLastUpdated());
    }
}