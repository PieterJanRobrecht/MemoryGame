package Controller;

import Lobby.ILobbyMethod;
import Model.User;
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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginController {

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField userNameText;

    private IRegistreerMethod implementation;
    private User user;

    @FXML
    void login(ActionEvent event) {
        String name = userNameText.getText();
        String pas = passwordText.getText();

        try {
            if (implementation.checkCredentials(name, pas)) {
                user = implementation.getUser(name);
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
        stage.setTitle("Welkom in de Memory Lobby");
        FXMLLoader loader = new FXMLLoader();

        try {
            //root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            root = (Parent) loader.load(getClass().getClassLoader().getResource("Lobby.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Ophalen van de controller horende bij de view klasse
        LobbyController lobbyController = loader.<LobbyController>getController();
        assert (lobbyController != null);

        registry(lobbyController);
    }

    private void registry(LobbyController lobbyController) {
        try{
            int serverpoort = implementation.getServer(user);
            Registry myRegistry = LocateRegistry.getRegistry ("localhost", serverpoort);

            ILobbyMethod impl = (ILobbyMethod) myRegistry.lookup("LobbyService");

            lobbyController.setImplementation(impl);
            System.out.println("Client verbonden met registry op poort "+serverpoort);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setImplementation(IRegistreerMethod implementation) {
        this.implementation = implementation;
    }
}