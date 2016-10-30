package Controller;

import java.util.Observable;

/**
 * Created by Pieter-Jan on 30/10/2016.
 */
public class LoginController extends Observable {
    public void registerClient() {
        System.out.println("Register new client");
        //TODO
    }

    public void forgotPassword() {
        System.out.println("Forgot Password");
        //TODO
    }

    public boolean checkCredentials(String username, String password) {
        System.out.println("Check credentials");
        //TODO goed schrijven, remote methode gebruiken voor te checken
        return true;
    }
    //    setChanged();
    //    notifyObservers();

}
