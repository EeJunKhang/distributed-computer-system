package model;

import enums.UserRole;
import java.io.Serializable;

public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected UserRole role;
    
    public Admin(int userId, String username, String passwordHash, String address, String contactNumber) {
        super(userId, username, passwordHash, address, contactNumber);
        role = UserRole.ADMIN;
    }

    public UserRole getRole() {
        return role;
    }
}
