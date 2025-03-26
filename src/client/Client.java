package client;

public class Client {
    //handle run ui

    public static void main(String args[]) {
        //check if session/token is correct
        boolean isSuccess = AuthClient.verifyToken();
        if (isSuccess) {

            // check user role, from 2 class
            new HomePage().setVisible(true);
            return;
        }
        new LoginPage().setVisible(true);
        
        
    }
}
