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
    private TableColumn<Game, Integer> serverColumn;

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
    private IGameMethod implementationGame;
    private User thisUser;
    private int serverId;
    private Stage thisStage;

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void showDetails(ActionEvent event) {
        setDetailsPaneInfo();
        detailsPane.setVisible(true);
        lobbyPane.setVisible(false);
    }

    private void setDetailsPaneInfo() {
        labelGewonnen.setText(thisUser.getAantalGewonnen() + "");
        labelVerloren.setText(thisUser.getAantalVerloren() + "");
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
            game.setServerId(serverId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (game != null) {
            setViewGameBuilder(game);
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
        serverColumn.setCellValueFactory(new PropertyValueFactory<Game, Integer>("serverId"));

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
                                            Game game = getTableView().getItems().get(getIndex());
                                            joinGame(game,false);
                                        }
                                        if (input.equals("Spectate")) {
                                            Game game = getTableView().getItems().get(getIndex());
                                            joinGame(game,true);
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

    private void joinGame(Game game, boolean spectator) {
        try {
            boolean result = false;
            if(!spectator) {
                if (serverId == game.getServerId()) {
                    result = implementation.addUserToGame(thisUser, game);

                } else {
                    //Migrate user to new server
                    implementation.removeUser(thisUser);
                    registry(game.getServerId());
                    serverId = game.getServerId();
                    implementation.addUser(thisUser);
                    result = implementation.addUserToGame(thisUser, game);
                }
            }
            if (!result && !spectator) {
                Notifications.create()
                        .title("ERROR")
                        .text("Maximum aantal spelers bereikt")
                        .showWarning();
            } else {
                if (serverId != game.getServerId()) {
                    //Migrate user to new server
                    implementation.removeUser(thisUser);
                    registry(game.getServerId());
                    serverId = game.getServerId();
                    implementation.addUser(thisUser);
                }

                thisUser.setSpectator(spectator);
                Stage stage = (Stage) lobbyPane.getScene().getWindow();
                stage.hide();
                setViewGame(game);
                //start met luisteren wat die moet doen
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setViewGame(Game game) throws RemoteException {
        registryGame();
        Stage stage = new Stage();
        Parent root = null;

        stage.setTitle("Game");
        FXMLLoader loader = new FXMLLoader();

        try {
            //root = FXMLLoader.load(getClass().getResource("Lobby.fxml"));
            root = (Parent) loader.load(getClass().getClassLoader().getResource("Game.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Ophalen van de controller horende bij de view klasse
        GameController gameController = loader.<GameController>getController();
        assert (gameController != null);

        gameController.setImplementation(implementationGame);
        gameController.setGame(game);
        gameController.setUser(thisUser);
        gameController.setLobbyStage((Stage) lobbyPane.getScene().getWindow());

        gameController.constructGrid();
        gameController.setOnExitAction();
    }


    public void startUpdateThreads() {
        try {
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
                                Platform.runLater(() -> playerList.appendText(name + "\r\n"));
                            }
                        }
                    }
                }
            }.start();

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setViewGameBuilder(Game game) {
        registryGame();
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
        gameBuilderController.setImplementation(implementationGame);
        gameBuilderController.initMakeGameView();

        thisStage = (Stage) lobbyPane.getScene().getWindow();
        thisStage.hide();

        gameBuilderController.setLobbyStage(thisStage);
        gameBuilderController.setOnExitAction();
        gameBuilderController.setUser(thisUser);
    }

    private void registryGame() {
        try {
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 45062 + serverId * 3 + 1);

            implementationGame = (IGameMethod) myRegistry.lookup("GameService");

            System.out.println("Client verbonden met game registryGame op poort " + serverId + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registry(int serverId){
        try {
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 45062 + serverId * 3);

            implementation = (ILobbyMethod) myRegistry.lookup("LobbyService");

            System.out.println("Client verbonden met game registryGame op poort " + serverId + 1);
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

    public void setServerId(int serverId) {
        this.serverId = serverId;
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
