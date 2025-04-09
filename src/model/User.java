/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.UserRole;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author C
 */

// modal mapping class, dont use directly, (inheriented)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String passwordSalt;
    private String email;
    private String address;
    private String contactNumber;
    private LocalDateTime createdTime;
    private UserRole role;


    // normal inheritance use (no role)
    public User(int userId, String firstName, String lastName, String username,
                String passwordHash, String passwordSalt, String email, String address,
                String contactNumber, LocalDateTime createdTime) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.address = address;
        this.contactNumber = contactNumber;
        this.createdTime = createdTime;
    }
    
    // register use (no userid, no role)
    public User(String firstName, String lastName, String username,
                String passwordHash, String passwordSalt, String email, String address,
                String contactNumber, LocalDateTime createdTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.address = address;
        this.contactNumber = contactNumber;
        this.createdTime = createdTime;
    }
    
    // empty constructor
    public User() {}

    // Getters
    public int getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getPasswordSalt() { return passwordSalt; } 
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public UserRole getRole() { return role; }
    public String getFullName(){ return this.firstName + " " + this.lastName; }

    
    //setter
    public void setUserId(int userId) { this.userId = userId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPasswordSalt(String passwordSalt) { this.passwordSalt = passwordSalt; } 
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public void setRole(UserRole role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + getFullName() + '\'' +
                ", username='" + username + '\'' +
                "" +
                '}';
    }


}