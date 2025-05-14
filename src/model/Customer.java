package model;

import enums.UserRole;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Customer(int userId, String firstName, String lastName, String username,
                 String passwordHash, String passwordSalt, String email, String address,
                 String contactNumber, LocalDateTime createdTime) {
        super(userId, firstName, lastName, username, passwordHash, passwordSalt,
              email, address, contactNumber, createdTime);
        setRole(UserRole.CUSTOMER);
    }
    
    public Customer(String firstName, String lastName, String username,
                    String passwordHash, String passwordSalt, String email, String address,
                    String contactNumber, LocalDateTime createdTime) {
        super(firstName, lastName, username, passwordHash, passwordSalt,
              email, address, contactNumber, createdTime);
        setRole(UserRole.CUSTOMER);
    }

    // default or placeholder for errors
    public Customer() {
        super(-1, "Temp", "Customer", "tempUser", "", "", 
                "temp@placeholder.com", "Unknown Address", 
                "0000000000", LocalDateTime.now());
        setRole(UserRole.CUSTOMER);
    }
}
