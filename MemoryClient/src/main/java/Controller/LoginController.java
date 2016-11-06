package Controller;

import Registreer.IRegistreerMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.rmi.RemoteException;

public class LoginController {

//    @FXML
//    private TextField passwordText;
    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField userNameText;

    private IRegistreerMethod implementation;

    @FXML
    void login(ActionEvent event) {
        String name = userNameText.getText();
        String pas = passwordText.getText();

        try {
            if (implementation.checkCredentials(name, pas)) {
                setViewLobby();
            } else {
                Notifications.create()
                        .title("ERROR")
                        .text("Gebruiksnaam en/of wachtwoord fout")
                        .showWarning();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void setViewLobby() {
        Stage stage;
        Parent root = null;

        //get reference to the button's stage
        stage = (Stage) userNameText.getScene().getWindow();
        stage.setTitle("Welkom in de memory lobby");

        try {
            //root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            FXMLLoader loader = new FXMLLoader();
            root = (Parent) loader.load(getClass().getClassLoader().getResource("Lobby.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setImplementation(IRegistreerMethod implementation) {
        this.implementation = implementation;
    }
}