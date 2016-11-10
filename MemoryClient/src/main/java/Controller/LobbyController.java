package Controller;

import Game.IGameMethod;
import Lobby.ILobbyMethod;
import Model.User;
import SpelLogica.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.Notifications;

import javax.swing.table.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class LobbyController {

    @FXML
    private BorderPane detailsPane;

    @FXML
    private TableView<Game> lobbyPane;

    @FXML
    private TableColumn<Game, String> nameColumn;

    @FXML
    private TableColumn<Game, Integer> numbersOfPlayersColumn;

    @FXML
    private TableColumn<Game, Integer> maxPlayersColumn;

    @FXML
    private TextArea globalChat;

    @FXML
    private TextArea lobbyChat;

    @FXML
    private TextArea playerList;

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
    private Stage thisStage;

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

        if (game != null) {
            setViewGame(game);
        } else {
            Notifications.create()
                    .title("ERROR")
                    .text("Kan geen nieuw spel maken")
                    .showWarning();
        }
    }

    @FXML
    void about(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        lobbyPane.setEditable(false);

        System.out.println("Initialize opgeroepen");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Game, String>("name"));
        maxPlayersColumn.setCellValueFactory(new PropertyValueFactory<Game, Integer>("maxAantalSpelers"));
        numbersOfPlayersColumn.setCellValueFactory(new PropertyValueFactory<Game, Integer>("aantalSpelers"));

        TableColumn actionCol = makeColumn("Join");
        lobbyPane.getColumns().add(actionCol);

        actionCol = makeColumn("Spectate");
        lobbyPane.getColumns().add(actionCol);
    }

    private TableColumn makeColumn(String input) {
        TableColumn actionCol = new TableColumn(input);
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Game, String>, TableCell<Game, String>> cellFactory = //
                new Callback<TableColumn<Game, String>, TableCell<Game, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Game, String> param) {
                        final TableCell<Game, String> cell = new TableCell<Game, String>() {

                            final Button btn = new Button(input);

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        if (input.equals("Join")) {
                                            //TODO kijken om view goed te zetten
                                            Game game = getTableView().getItems().get(getIndex());
                                            //implementation.addUserToGame();
                                            //start met luisteren wat die moet doen
                                        }
                                        if (input.equals("Spectate")) {
                                            //TODO kom in spectator mode
                                            //Gewoon gebruik maken van de kijk methode?
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionCol.setCellFactory(cellFactory);
        return actionCol;
    }


    public void startUpdateThreads() {
        new Thread() {
            public void run() {
                while (true) {
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
            }
        }.start();
        try {
            new Thread() {
                public void run() {
                    while (true) {
                        List<Game> games = null;

                        try {
                            games = implementation.getRunningGames();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        if (games != null) {
                            lobbyPane.getItems().setAll(games);
                        }
                    }
                }
            }.start();
        }catch(Exception e){
            System.out.println(e.getMessage());
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

        thisStage = (Stage) lobbyPane.getScene().getWindow();
        thisStage.hide();

        gameBuilderController.setLobbyStage(thisStage);
        gameBuilderController.setOnExitAction();
        gameBuilderController.setUser(thisUser);
    }

    private void registry(GameBuilderController gameBuilderController) {
        try {
            Registry myRegistry = LocateRegistry.getRegistry("localhost", serverPoort + 1);

            IGameMethod impl = (IGameMethod) myRegistry.lookup("GameService");

            gameBuilderController.setImplementation(impl);
            gameBuilderController.initMakeGameView();

            System.out.println("Client verbonden met game registry op poort " + serverPoort + 1);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void setOnExitAction() {
        Stage stage = (Stage) lobbyPane.getScene().getWindow();
        stage.setOnCloseRequest(we -> {
            try {
                implementation.logOutUser(thisUser);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
    }
}
