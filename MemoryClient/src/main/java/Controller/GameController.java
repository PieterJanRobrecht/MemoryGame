package Controller;

import Game.IGameMethod;
import Model.User;
import SpelLogica.Game;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

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
    private Label huidigeSpelers;

    @FXML
    private GridPane speelveld;

    private Stage lobbyStage;
    private User user;

    //private final EventHandler imageViewClickEventHandler = clickEventHandler();

    public void constructGrid() throws RemoteException {

        int grootte = game.getGrootteVeld();
        grootteSpel.setText(grootte + " op " + grootte);

        startAantalSpelersThread();

        byte[] bytes = implementation.getBackgroundImage(game.getThema());
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backImage = SwingFXUtils.toFXImage(img, null );

        for(int i=0; i<grootte;i++){
            for(int j=0; j<grootte; j++){
                ImageView imageView = new ImageView(backImage);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setEffect(new DropShadow(5, Color.BLACK));

//                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
                speelveld.add(imageView, i, j);
            }
        }

        //Ophalen foto's van db
        //Haal info op over waar welke figuren komen
        //Maken van de nodige click listeners
        //Starten van thread die luistert naar de server

    }

    //Thread maken die vraagt aan de server wat die moet doen
    //Server antwoord ofwel -> gok ofwel -> kijk

    public void startAantalSpelersThread(){
        new Thread(){
            List<User> gebruikers;
            String string;
            public void run() {
                while (true) {
                    string = "";
                    try {
                        gebruikers = implementation.getGameById(game.getGameId()).getGamers();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    if (gebruikers != null){
//                        gebruikers.clear();
                        for (User name : gebruikers){
                            string += name.getNaam() + "\r\n";
                        }

                    }
                    huidigeSpelers.setText(string);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void setImplementation(IGameMethod implementation) {
        this.implementation = implementation;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setOnExitAction() {
        Stage stage = (Stage) speelveld.getScene().getWindow();
        stage.setOnCloseRequest(we -> {
            lobbyStage.show();

            try {
                implementation.releaseGame(game,user);
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("Error after clossing game");
            }

            stage.close();
        });
    }

    public void setLobbyStage(Stage lobbyStage) {
        this.lobbyStage = lobbyStage;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
