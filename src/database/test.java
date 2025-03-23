
package database;

import enums.UserRole;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import model.User;
import model.Items;


public class test {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) throws SQLException {
        
//*************************ITEM********************* 
        //***********get All Item
//        ItemDAO itemDAO = new ItemDAO();
//        List<Items> items = itemDAO.getAllProducts();
//        for (Items item : items) {
//            System.out.println(item);
//        }
        
        //*************update item
//        Items item = itemDAO.getProductById(1); 
//        item.setItemName("Coffee");
//       
//        itemDAO.updateProduct(item);
        
        //*************add Item
//        Items newItem = new Items();
//        newItem.setItemName("Chocolate Donut");
//        newItem.setItemDescription("Delicious chocolate-glazed donut.");
//        newItem.setPrice(2.99);
//        newItem.setCategory("Bakery");
//        newItem.setImageUrl("https://example.com/donut.jpg");
//        newItem.setStockQuantity(50);
//
//        itemDAO.addItem(newItem);
            
        //***************delete Item
//          itemDAO.deleteItem(3);

//*************************USER*********************      
        //***************get user
//        UserDAO userDAO = new UserDAO();
//        User user = userDAO.getUserById(1);
        
//        if (user != null) {
//            System.out.println("User found: " + user.getFirstName() + " " + user.getLastName());
//            System.out.println("Role: " + user.getRole());
//        } else {
//            System.out.println("No user found with the given ID.");
//        }
        //a*****************add user
//        User testUser = new User();
//        testUser.setFirstName("John");
//        testUser.setLastName("Doe");
//        testUser.setUsername("liu");
//        testUser.setPasswordHash("hashedPassword123");
//        testUser.setEmail("john.doe@hotlink.com");
//        testUser.setAddress("123 Main St");
//        testUser.setContactNumber("1234567890");
//        testUser.setRole(UserRole.CUSTOMER);
//        
//        UserDAO userService = new UserDAO();
//        
//        userService.createUser(testUser);


   }
   
}
