package model;

import enums.UserRole;
import java.io.Serializable;

public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected UserRole role;
    
    public Customer(int userId, String username, String passwordHash, String address, String contactNumber) {
        super(userId, username, passwordHash, address, contactNumber);
        role = UserRole.CUSTOMER;
    }

    public UserRole getRole() {
        return role;
    }
}
