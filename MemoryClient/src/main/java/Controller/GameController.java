package Controller;

import Game.IGameMethod;
import Model.User;
import SpelLogica.Game;
import SpelLogica.Move;
import ViewThread.CardThread;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
    private Map<Integer, Image> images;
    private Move move;
    private int buzzyUserID, index=0;
    private List<Integer> gevondenImages;
    private final EventHandler imageViewClickEventHandler = clickEventHandler();
    private boolean afgelopen = false;

    @FXML
    private Label grootteSpel;

    @FXML
    private Label huidigeSpelers;

    @FXML
    private GridPane speelveld;

    @FXML
    private Label afgelopenText;

    private Stage lobbyStage;
    private User user;


    public void constructGrid() throws RemoteException {

        int grootte = game.getGrootteVeld();
        grootteSpel.setText(grootte + " op " + grootte);
        buzzyUserID = implementation.getbuzzyUserID(game.getGameId(), -2);

        byte[] bytes = implementation.getBackgroundImage(game.getThema());
        BufferedImage img = null;
        try {
            img = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
        backImage = SwingFXUtils.toFXImage(img, null);

        for (int i = 0; i < grootte; i++) {
            for (int j = 0; j < grootte; j++) {
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
        gevondenImages = new ArrayList<Integer>();
        if (IDs == null) {
            System.out.println("fout met de IDs");
        } else {
            for (int ID : IDs) {
                bytes = implementation.getImage(ID);
                img = null;
                try {
                    img = ImageIO.read(new ByteArrayInputStream(bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                images.put(ID, SwingFXUtils.toFXImage(img, null));
            }
        }

        move = new Move();

        startTreads();
    }

    private EventHandler clickEventHandler() { //handler for all click events
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                if (afgelopen) {
                    Platform.runLater(() -> afgelopenText.setText("STOP AUB."));
                    Stage stage = (Stage) speelveld.getScene().getWindow();
                    closeGameView(stage);
                }
                if(user.getSpectator()) return;
                if (!(event.getSource() instanceof ImageView)) return;
                if (buzzyUserID != user.getId()) return;
                try {
                    if (!(implementation.voldoendeSpelers(game.getGameId()))) return;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                ImageView clickedImageView = (ImageView) event.getSource();

                int col = GridPane.getColumnIndex(clickedImageView);
                int row = GridPane.getRowIndex(clickedImageView);

                int afbeeldingID = game.getVeld()[col][row];
                try {
                    if (implementation.addCardToMove(col, row, game.getGameId(), index)) {  //kan ook omgekeerd zijn
                        Platform.runLater(new CardThread(clickedImageView, images.get(afbeeldingID)));
                        System.out.println(" afbeelding weergeven");
                    }
                    move=implementation.getMove(game.getGameId(), index);

                }catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (move.isCompleet()) {
                    try {
                        int index1 = move.getCardX1() * game.getGrootteVeld() + move.getCardY1();
                        int index2 = move.getCardX2() * game.getGrootteVeld() + move.getCardY2();

                        if (implementation.doMove(game.getGameId(), user.getId(), move)) {
                            speelveld.getChildren().get(index1).removeEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
                            speelveld.getChildren().get(index2).removeEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);

                            gevondenImages.add(afbeeldingID);
                            if(implementation.isGameDone(gevondenImages.size(), game.getGameId())){
                                gameAfsluiten();
                            }
                        }
                        else {
                            System.out.println("We gaan nu wachten om de kaarten terug om te draaien");
                            TimeUnit.SECONDS.sleep(2);
                            ImageView first = (ImageView) speelveld.getChildren().get(index1);
                            ImageView second = (ImageView) speelveld.getChildren().get(index2);

                            Platform.runLater(new CardThread(first,backImage));
                            Platform.runLater(new CardThread(second, backImage));

                            implementation.setNextBuzzyUser(game.getGameId());
                        }
                        implementation.resetMove(game.getGameId());
                        index++;
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

    public void toonAfbeelding(int afbeeldingId) {
        int[][] veld = game.getVeld();
        int lengte = veld.length;
        for (int i = 0; i < lengte; i++) {
            for (int j = 0; j < lengte; j++) {
                if (veld[i][j] == afbeeldingId) {
                    int index = i * game.getGrootteVeld() + j; //kan ook omgekeerd zijn
                    ImageView view = (ImageView) speelveld.getChildren().get(index);
                    Platform.runLater(new CardThread(view,images.get(afbeeldingId)));
                    speelveld.getChildren().get(index).removeEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
                }
            }
        }
    }

    public void startTreads() {
        new Thread() {
            public void run() {
                int coordTempImage[]= new int[2];//0=x,1=y
                int indexOud, index1, index2, afbeeldingId1, afbeeldingId2, x, y;

                while (!afgelopen){
                    try {
                        buzzyUserID = implementation.getbuzzyUserID(game.getGameId(), buzzyUserID);
                        if (buzzyUserID == user.getId() && !user.getSpectator()) {
                            Platform.runLater(() -> afgelopenText.setText("Jij bent aan de beurt..."));
                            System.out.println("jij bent aan de beurt");
                            indexOud = index;
                            while(indexOud == index){
                                Thread.sleep(20);
                            }
                        }
                        else {
                            if(user.getSpectator()) {
                                Platform.runLater(() -> afgelopenText.setText("Kijk naar het spelverloop."));
                            }
                            else {
                                Platform.runLater(() -> afgelopenText.setText("Wacht op jouw beurt..."));
                            }
                            System.out.println("Iemand anders is aan de beurt");


                            coordTempImage = implementation.getCoordFromMove(game.getGameId(),index, 1);
                            x = coordTempImage[0];
                            y = coordTempImage[1];
                            afbeeldingId1 = game.getVeld()[x][y];
                            index1 = x*game.getGrootteVeld()+y;

                            ImageView first = (ImageView)speelveld.getChildren().get(index1);
                            Platform.runLater(new CardThread(first,images,afbeeldingId1));

                            coordTempImage = implementation.getCoordFromMove(game.getGameId(),index , 2);
                            x = coordTempImage[0];
                            y = coordTempImage[1];
                            afbeeldingId2 = game.getVeld()[x][y];
                            index2 = x*game.getGrootteVeld()+y;

                            ImageView second = (ImageView)speelveld.getChildren().get(index2);
                            Platform.runLater(new CardThread(second,images,afbeeldingId2));

                            if(afbeeldingId1 == afbeeldingId2){
                                if(!gevondenImages.contains(afbeeldingId1)) {//test laatste moment
                                    gevondenImages.add(afbeeldingId1);
                                }
                                if(implementation.isGameDone(gevondenImages.size(), game.getGameId())){
                                    gameAfsluiten();
                                }
                            }
                            else {
                                TimeUnit.SECONDS.sleep(2);
                                first = (ImageView) speelveld.getChildren().get(index1);
                                Platform.runLater(new CardThread(first,backImage));

                                second = (ImageView) speelveld.getChildren().get(index2);
                                Platform.runLater(new CardThread(second,backImage));
                            }
                            index++;

                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
        new Thread() { //voor wergeven spelers naast spel
            List<User> gebruikers;
            String string;

            public void run() {
                while (true) {
                    string = "";
                    Game thisGame =null;
                    try {
                        thisGame = implementation.getGame(game.getGameId());
                        gebruikers = thisGame.getGamers();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    if (gebruikers != null) {
                        //gebruikers.clear();
//                        System.out.println("This user= "+user.getNaam()+ " Aantal users= " + gebruikers.size());
                        for (User name : gebruikers) {
                            string += name.getNaam() + " " + thisGame.getPunten(name.getId()) + "\r\n";
                        }
                    }
                    Platform.runLater(() -> huidigeSpelers.setText(string));
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
            closeGameView(stage);
            try {
                if (!afgelopen) {//user verwijderen uit game
                    implementation.removeUser(game.getGameId(), user);
                }
            }catch(RemoteException e){
                e.printStackTrace();
            }
        });
    }

    public void closeGameView(Stage stage){
        lobbyStage.show();

        if(!user.getSpectator()) {
            try {
                implementation.releaseGame(game, user);
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("Error after clossing game");
            }
        }

        user.setSpectator(false);
        stage.close();
    }

    public void setLobbyStage(Stage lobbyStage) {
        this.lobbyStage = lobbyStage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void gameAfsluiten() {
        try {
            afgelopen = true;
            String afscheidText = implementation.getWinner(game.getGameId());
            String[] split = afscheidText.split(" ");

            if(!user.getSpectator()){
                if (user.getNaam().equals(split[3])) {
                    user.verhoogAantalWinnen();
                }else{
                    user.verhoogAantalVerliezen();
                }

            }

            Platform.runLater(() -> afgelopenText.setText(afscheidText + " Klik hier om terug naar de lobby te gaan."));
            afgelopenText.addEventHandler(MouseEvent.MOUSE_CLICKED, imageViewClickEventHandler);
            System.out.println("Game is afgelopen");

        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Error after clossing game");
        }
    }

}
