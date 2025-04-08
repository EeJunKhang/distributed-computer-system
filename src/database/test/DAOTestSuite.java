/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package database.test;


/**
 *
 * @author C
 * A simple test suite that runs all DAO tests
 */
public class DAOTestSuite {
    
    public static void main(String[] args) {
        System.out.println("\n\n=== STARTING USER DAO TEST ===");
        UserDAOTest.testUserDAO();
        System.out.println("=== STARTING PRODUCTS DAO TEST ===");
        ProductsDAOTest.testProductDAO();
        System.out.println("\n\n=== STARTING ORDER DAO TEST ===");
        OrderDAOTest.testOrderDAO();
        System.out.println("\n\n=== STARTING ORDER ITEM DAO TEST ===");
        OrderItemDAOTest.testOrderItemDAO();
        System.out.println("\n\n=== ALL TESTS COMPLETED ===");
    }
}
