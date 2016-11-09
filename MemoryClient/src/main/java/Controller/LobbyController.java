package Controller;

import Lobby.ILobbyMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.List;

import static java.lang.Thread.sleep;

public class LobbyController {

    @FXML
    private TableView<?> lobbyPane;

    @FXML
    private BorderPane detailsPane;

    @FXML
    private TableColumn<?, ?> numbersOfPlayersColumn;

    @FXML
    private TableColumn<?, ?> joinColumn;

    @FXML
    private TextArea globalChat;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextArea lobbyChat;

    @FXML
    private TextArea playerList;

    @FXML
    private TableColumn<?, ?> maxPlayersColumn;

    @FXML
    private TableColumn<?, ?> spectateColumn;

    @FXML
    private TextField sendTextField;

    @FXML
    private Label labelGespeeld;

    @FXML
    private Label labelVerloren;

    @FXML
    private Label labelGewonnen;

    private ILobbyMethod implementation;

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void showDetails(ActionEvent event) {
        detailsPane.setVisible(true);
        lobbyPane.setVisible(false);
    }

    @FXML
    void backToLobby(ActionEvent event) {
        detailsPane.setVisible(false);
        lobbyPane.setVisible(true);
    }

    @FXML
    void logOut(ActionEvent event) {

    }

    @FXML
    void makeNewGame(ActionEvent event) {

    }

    @FXML
    void about(ActionEvent event) {

    }

    public void updatePlayerList(){
        //TODO Goed implementeren zodanig dat deze blijft updaten
        List<String> users = null;

        try {
            users = implementation.getUserNames();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (users != null) {
            playerList.clear();
            for (String name :
                    users) {
                playerList.appendText(name + "\r\n");
            }
        }
    }

    public void setImplementation(ILobbyMethod implementation) {
        this.implementation = implementation;
    }
}
