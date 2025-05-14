/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.UserRole;
import java.io.Serializable;

public class AuthResult implements Serializable{

    private String errorMessage;
    private AuthToken token;
    private UserRole userRole;

    public AuthResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AuthResult(AuthToken token) {
        this.token = token;
    }

    public AuthResult(UserRole userRole, AuthToken token) {
        this.token = token;
        this.userRole = userRole;
    }
    
    public AuthResult(){
        
    }

    public boolean hasError() {
        return errorMessage != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public AuthToken getToken() {
        return this.token;
    }
}
