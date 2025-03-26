/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.UserRole;

/**
 *
 * @author C
 */

// modal mapping class, dont use directly, (inheriented)
public class User{
//    private static final long serialVersionUID = 1L;

    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String email;
    private String address;
    private String contactNumber;
//    private UserRole role;
    private String createdTime;
//    private UserRole role;
    
    public User(int userId,String firstName,String lastName, String username, String passwordHash, 
            String email,String address, String contactNumber,String createdTime) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.address = address;
        this.contactNumber = contactNumber;
        this.createdTime = createdTime;
    }
    
    //for register
    public User(String firstName,String lastName, String username, String passwordHash, 
            String email,String address, String contactNumber,UserRole role,String createdTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.address = address;
        this.contactNumber = contactNumber;
        this.createdTime = createdTime;
    }
    

    public User() {
        
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    
    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

//    public UserRole getRole() {
//        return role;
//    }
    
    public String getCreatedTime() {
        return createdTime;
    }
    
    //setter
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

//    public void setRole(UserRole role) {
//        this.role = role;
//    }
    
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

//    public void setRole(UserRole role) {
//        this.role = role;
//    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
//                ", role=" + role +
                '}';
    }
}