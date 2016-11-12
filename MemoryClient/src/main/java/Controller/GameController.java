package Controller;

import Game.IGameMethod;
import Model.User;
import SpelLogica.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public class GameController {

    private IGameMethod implementation;
    private Game game;
    private Image backImage;

    @FXML
    private Label grootteSpel;

    @FXML
    private Label nSpelers;

    @FXML
    private GridPane memoryCardGrid;

    private Stage lobbyStage;
    private User user;

    //private final EventHandler imageViewClickEventHandler = clickEventHandler();

    public void constructGrid() throws RemoteException {

        int grootte = game.getGrootteVeld();
        grootteSpel.setText(grootte + " op " + grootte);

        int nSp = game.getAantalSpelers();
        int maxSp = game.getMaxAantalSpelers();
        nSpelers.setText(nSp + " van de "+maxSp);

        //backImage = implementation.getBackgroundImage();

//        int grootte = game.getGrootteVeld();
//        for(int i=0; i<grootte;i++){
//            for(int j=0; j<grootte; j++){
//                //ImageView imageView = new ImageView(backImage);
//                ImageView imageView = new ImageView("test.jpg");//voorlopig op te testen
//                imageView.setFitWidth(100);
//                imageView.setFitHeight(100);
//                imageView.setEffect(new DropShadow(5, Color.BLACK));
//
//                //imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
//
//                memoryCardGrid.add(imageView, 0, 0);
//            }
//
//        }

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
