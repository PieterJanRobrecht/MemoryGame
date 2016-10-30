package UI;

import Controller.LoginController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Pieter-Jan on 30/10/2016.
 */
public class LoginView implements Observer {

    private LoginController loginController;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    void doRegister(ActionEvent event) {
        loginController.registerClient();
    }

    @FXML
    void doForgot(ActionEvent event) {
        loginController.forgotPassword();
    }

    @FXML
    void doLogin(ActionEvent event) {
        String username = null, password = null;

        if (!usernameField.getText().equals("")) {
            username = usernameField.getText();
        } else {
            //TODO notification voor geen naam
        }

        if (!passwordField.getText().equals("")) {
            password = passwordField.getText();
        } else {
            //TODO notification voor geen passwoord
        }

        do {
            //TODO notification dat je iets moet invullen
        } while (!loginController.checkCredentials(username, password));

        closeLoginView();

        openLobbyView();
    }

    private void closeLoginView() {
        Platform.exit();
    }

    private void openLobbyView() {

    }

    /**
     * Updaten van de Lobby View
     */
    public void update(Observable o, Object arg) {

    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
