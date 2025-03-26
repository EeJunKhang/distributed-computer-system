package model;

import enums.UserRole;
import java.io.Serializable;

public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public Admin(int userId, String firstName, String lastName, String username, String passwordHash, 
                 String email, String address, String contactNumber, String createdTime) {
        super(userId, firstName, lastName, username, passwordHash, email, address, contactNumber, createdTime);
    }
}
