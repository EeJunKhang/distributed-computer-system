package model;

import enums.UserRole;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Admin(int userId, String firstName, String lastName, String username,
                    String passwordHash, String passwordSalt, String email, String address,
                    String contactNumber, LocalDateTime createdTime) {
        super(userId, firstName, lastName, username, passwordHash, passwordSalt,
                email, address, contactNumber, createdTime);
        setRole(UserRole.ADMIN);
    }
}