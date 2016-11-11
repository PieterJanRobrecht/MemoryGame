package Controller;

import Game.IGameMethod;
import Model.User;
import SpelLogica.Game;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public class GameController {

    private IGameMethod implementation;
    private Game game;

    @FXML
    private GridPane memoryCardGrid;
    private Stage lobbyStage;
    private User user;


    public void makeFieldWithInfoOfServer() {
        //Ophalen foto's van db
        //Haal info op over waar welke figuren komen
        //Maken van de nodige click listeners
        //Starten van thread die luistert naar de server
    }

    //Thread maken die vraagt aan de server wat die moet doen
    //Server antwoord ofwel -> gok ofwel -> kijk

    public void setImplementation(IGameMethod implementation) {
        this.implementation = implementation;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setOnExitAction() {
        Stage stage = (Stage) memoryCardGrid.getScene().getWindow();
        stage.setOnCloseRequest(we -> {
            lobbyStage.show();

            try {
                implementation.releaseGame(game,user);
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("Error after clossing game");
            }

            Stage stageBuilder = (Stage) memoryCardGrid.getScene().getWindow();
            stageBuilder.close();
        });
    }

    public void setLobbyStage(Stage lobbyStage) {
        this.lobbyStage = lobbyStage;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
