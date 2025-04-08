package model;

import java.io.Serializable;

public class _RegisterCredential implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String address;
    private String contactNumber;
    
    public _RegisterCredential(String firstName, String lastName, String username, 
                              String password, String email, String address, 
                              String contactNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }
    
    @Override
    public String toString() {
        return "RegisterCredential{firstName='" + firstName + "', lastName='" + lastName + 
                "', username='" + username + "', email='" + email + "'}";
    }
}
