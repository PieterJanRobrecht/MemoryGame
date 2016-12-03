package Controller;

import Dispatcher.IDispatcherMethod;
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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField userNameText;

    private IRegistreerMethod implementation;
    private User user;
    private int serverId;

    @FXML
    void login(ActionEvent event) {
        String name = userNameText.getText();
        String pas = passwordText.getText();
        pas = hashPas(pas);

        try {
            if (implementation.checkCredentials(name, pas)) {
                user = implementation.getUser(name, user);
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

    private String hashPas(String pas) {
        StringBuilder stringBuffer = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(pas.getBytes());
            byte[] messageDigestMD5 = md.digest();
            for (byte bytes : messageDigestMD5) {
                stringBuffer.append(String.format("%02x", bytes & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @FXML
    void registerAccount(ActionEvent event) {
        String name = userNameText.getText();
        String pas = passwordText.getText();
        pas = hashPas(pas);

        try {
            if (name.equals("") || pas.equals("")) {
                Notifications.create()
                        .title("ERROR")
                        .text("Gelieve iets in te vullen")
                        .showWarning();

            } else if (implementation.checkCredentials(name, pas)) {
                Notifications.create()
                        .title("ERROR")
                        .text("Gebruikersnaam bestaat al")
                        .showWarning();
            } else {
                if (implementation.createAccount(name,pas)){
                    Notifications.create()
                            .title("INFO")
                            .text("Gebruiker aangemaakt")
                            .showInformation();
                }
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
        lobbyController.setThisUser(user);
        lobbyController.setOnExitAction();

        connectDispatcher();
    }

    private void connectDispatcher() {
        try {
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 45016);
            IDispatcherMethod dispatcher = (IDispatcherMethod) myRegistry.lookup("DispatcherService");
            dispatcher.updateUserServer(user, serverId);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void registry(LobbyController lobbyController) {
        try {
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 45062 + serverId * 3);

            ILobbyMethod impl = (ILobbyMethod) myRegistry.lookup("LobbyService");

            lobbyController.setImplementation(impl);

            //Zodanig dat de server weet welke user er bij hem zit
            impl.addUser(user);
            lobbyController.startUpdateThreads();
            lobbyController.setServerId(serverId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImplementation(IRegistreerMethod implementation) {
        this.implementation = implementation;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}