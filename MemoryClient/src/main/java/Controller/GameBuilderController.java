package Controller;

import Game.IGameMethod;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import SpelLogica.Game;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
import java.io.IOException;
import java.rmi.RemoteException;

public class GameBuilderController {

    private Stage lobbyStage;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> sizeDropDown;

    @FXML
    private ComboBox<String> playersDropDown;

    @FXML
    private ComboBox<String> themeDropDown;
    private IGameMethod implementation;

    private final int AANTAL_SPELERS = 2; //Wordt maal 2 gedaan
    private final int GROOTTE_VELD = 2; //1 = 2x2 , 2= 2x2 en 4x4, 3= 2x2 4x4 8x8
    private final int AANTAL_THEMAS = 2;
    private Game game;
    private User user;

    @FXML
    void cancel(ActionEvent event) {
        closeWindow();
    }

    @FXML
    void makeGame(ActionEvent event) throws RemoteException {
        if (setValues()) {
            try {
                implementation.makeGame(game);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            startGameWindow();
        }
    }

    private boolean setValues() {
        boolean alles = true;

        String name = nameField.getText();
        if (!name.equals("")) {
            game.setName(name);
        } else {
            alles = false;
            createMessage("Gelieve een naam in te geven");
        }

        String output = playersDropDown.getSelectionModel().getSelectedItem();
        if (output == null) {
            alles = false;
            createMessage("Aantal spelers te selecteren");
        } else {
            String[] split = output.split(" ");
            game.setMaxAantalSpelers(Integer.parseInt(split[0]));
        }

        output = sizeDropDown.getSelectionModel().getSelectedItem();
        if (output == null) {
            alles = false;
            createMessage("Gelieve grootte te selecteren");
        } else {
            String [] split = output.split("x");
            game.setGrootteVeld(Integer.parseInt(split[0]));
        }

        output = themeDropDown.getSelectionModel().getSelectedItem();
        if (output == null) {
            alles = false;
            createMessage("Gelieve een thema te selecteren");
        } else {
            game.setThema(output);
        }

        return alles;
    }

    private void createMessage(String s) {
        Notifications.create()
                .title("ERROR")
                .text(s)
                .showWarning();
    }

    private void startGameWindow() throws RemoteException {
        Stage stage;
        Parent root = null;

        //get reference to the button's stage
        stage = (Stage) sizeDropDown.getScene().getWindow();
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

        gameController.setImplementation(implementation);
        gameController.setGame(game);
        gameController.setUser(user);
        gameController.setLobbyStage(lobbyStage);

        gameController.constructGrid();
        gameController.setOnExitAction();
    }

    public void initMakeGameView() {

        String[] themas = {"Fruit", "Car"};

        for (int i = 1; i < AANTAL_SPELERS + 1; i++) {
            playersDropDown.getItems().add(i * 2 + " Spelers");
        }

        for (int i = 1; i < GROOTTE_VELD + 1; i++) {
            int grootte = (int) Math.pow(2, i);
            sizeDropDown.getItems().add(grootte + "x" + grootte);
        }

        for (int i = 0; i < AANTAL_THEMAS; i++) {
            themeDropDown.getItems().add(themas[i]);
        }
    }

    public void setImplementation(IGameMethod implementation) {
        this.implementation = implementation;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setLobbyStage(Stage lobbyStage) {
        this.lobbyStage = lobbyStage;
    }

    public void setOnExitAction() {
        Stage stage = (Stage) playersDropDown.getScene().getWindow();
        stage.setOnCloseRequest(we -> closeWindow());
    }

    public void closeWindow() {
        lobbyStage.show();

        try {
            implementation.releaseGame(game, user);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Stage stageBuilder = (Stage) playersDropDown.getScene().getWindow();
        stageBuilder.close();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
