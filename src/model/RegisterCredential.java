package model;

import java.io.Serializable;

public class RegisterCredential implements Serializable {

    private final String username;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String contactNumber;
    private final String email;

    public RegisterCredential(String username, String password, String firstName, String lastName, String address, String contactNumber, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }
    
    
}
