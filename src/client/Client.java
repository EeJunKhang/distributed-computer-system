package client;

import client.Interface.LoginPage;
import client.Interface.HomePage;
import client.Interface.AdminPage;
import enums.UserRole;
import model.AuthToken;
import model.User;
import utils.TokenStorage;

public class Client {
    //handle run ui

    public static void main(String args[]) {
        //check if session/token is correct
        AuthToken token = TokenStorage.loadToken();// get saved token from local
        if (token != null) {
            User user = new AuthClient().validateSession(token);
            if (user != null) {
                // fetch user info hll) {
                // fetch usere
                // == if needed
                if (user.getRole() == UserRole.ADMIN) {
                    new AdminPage(token).setVisible(true);
                    return;
                } else {
                    new HomePage(token).setVisible(true);
                    return;
                }
            }
        }
        new LoginPage().setVisible(true);

    }
}
