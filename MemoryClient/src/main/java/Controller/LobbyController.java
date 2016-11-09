package Controller;

import Game.IGameMethod;
import SpelLogica.Game;
import Lobby.ILobbyMethod;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class LobbyController {

    @FXML
    private AnchorPane mainPane;

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
    private User thisUser;
    private int serverPoort;

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
        //Vragen aan server voor het maken van een game
        Game game = null;

        try {
            game = implementation.canMakeGame(thisUser);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(game != null) {
            setViewGame(game);
        }else{
            Notifications.create()
                    .title("ERROR")
                    .text("Kan geen nieuw spel maken")
                    .showWarning();
        }
    }

    private void setViewGame(Game game) {
        Stage stage = new Stage();
        Parent root = null;

        stage.setTitle("Spel maken");
        FXMLLoader loader = new FXMLLoader();

        try {
            //root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            root = (Parent) loader.load(getClass().getClassLoader().getResource("GameBuilder.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Ophalen van de controller horende bij de view klasse
        GameBuilderController gameBuilderController = loader.<GameBuilderController>getController();
        assert (gameBuilderController != null);

        gameBuilderController.setGame(game);
        registry(gameBuilderController);
    }

    private void registry(GameBuilderController gameBuilderController) {
        try{
            Registry myRegistry = LocateRegistry.getRegistry ("localhost", serverPoort+1);

            IGameMethod impl = (IGameMethod) myRegistry.lookup("GameService");

            gameBuilderController.setImplementation(impl);
            gameBuilderController.initMakeGameView();
            mainPane.setVisible(false);
            gameBuilderController.setMainPane(mainPane);

            System.out.println("Client verbonden met game registry op poort "+serverPoort+1);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
    }

    public void setImplementation(ILobbyMethod implementation) {
        this.implementation = implementation;
    }

    public void setServerPoort(int serverPoort) {
        this.serverPoort = serverPoort;
    }
}
