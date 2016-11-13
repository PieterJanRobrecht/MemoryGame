package Controller;

import Game.IGameMethod;
import Model.User;
import SpelLogica.Game;
import SpelLogica.Move;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public class GameController {

    private IGameMethod implementation;
    private Game game;
    private Image backImage;
    private Map <Integer, Image> images;
    private Move move;

    private final EventHandler imageViewClickEventHandler = clickEventHandler();

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

                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
                speelveld.add(imageView, i, j);
            }
        }

        //Ophalen foto's van db

        game = implementation.getGame(game.getGameId()); //nodig omdat de verandering om de game op server niet aangepast zijn op de lokale kopie!
        List<Integer> IDs = game.getImageIDs();
        images = new HashMap<Integer, Image>();
        if(IDs == null){
            System.out.println("fout met de IDs");
        }
        else {
            for (int ID : IDs) {
                bytes = implementation.getImage(ID);
                img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                images.put(ID, SwingFXUtils.toFXImage(img, null));
                System.out.println("foto op client opgeslagen met id " + ID);
            }
        }

        move = new Move();

        //Haal info op over waar welke figuren komen
        //Maken van de nodige click listeners
        //Starten van thread die luistert naar de server

    }

    private EventHandler clickEventHandler() {
        /**
         * handler for all click events
         */
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!(event.getSource() instanceof ImageView)) return;

                ImageView clickedImageView = (ImageView) event.getSource();

                int col = GridPane.getColumnIndex(clickedImageView);
                int row = GridPane.getRowIndex(clickedImageView);

//                int selectedCardIndex = row * CARDS_PER_ROW + col;

                int afbeeldingID = game.getVeld()[col][row];
                if (move.addCardToMove(col, row)) {  //kan ook omgekeerd zijn
                    clickedImageView.setImage(images.get(afbeeldingID));
                }

                if (move.isCompleet()) {
                    try {
                        int index1 = move.getCardX1()*game.getGrootteVeld()+move.getCardY1();
                        int index2 = move.getCardX2()*game.getGrootteVeld()+move.getCardY2();

                        if (implementation.doMove(game.getGameId(), user, move)) {
                            speelveld.getChildren().get(index1).removeEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
                            speelveld.getChildren().get(index2).removeEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
                        }
                        else {
                            TimeUnit.SECONDS.sleep(2);
                            ((ImageView)speelveld.getChildren().get(index1)).setImage(backImage);
                            ((ImageView)speelveld.getChildren().get(index2)).setImage(backImage);
                        }
                        move = new Move();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
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
                        gebruikers = implementation.getGame(game.getGameId()).getGamers();
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
