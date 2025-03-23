package model;

import enums.UserRole;
import java.io.Serializable;

public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected UserRole role;
    
    public Customer(int userId, String firstName, String lastName, String username, String passwordHash, 
                 String email, String address, String contactNumber, String createdTime) {
        super(userId, firstName, lastName, username, passwordHash, email, address, contactNumber, UserRole.ADMIN, createdTime);
    }

    public UserRole getRole() {
        return role;
    }
}
