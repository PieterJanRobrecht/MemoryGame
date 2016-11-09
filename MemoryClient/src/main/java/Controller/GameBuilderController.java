package Controller;

import Game.IGameMethod;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import SpelLogica.Game;

import java.rmi.RemoteException;

public class GameBuilderController {

    private AnchorPane mainPane;

    @FXML
    private ComboBox<String> sizeDropDown;

    @FXML
    private ComboBox<String> playersDropDown;

    @FXML
    private ComboBox<String> themeDropDown;
    private IGameMethod implementation;

    private final int AANTAL_SPELERS = 1; //Wordt maal 2 gedaan
    private final int GROOTTE_VELD = 1; //1 = 2x2 , 2= 2x2 en 4x4, 3= 2x2 4x4 8x8
    private final int AANTAL_THEMAS = 1;
    private Game game;

    @FXML
    void cancel(ActionEvent event) {
        //TODO delete game van server
        mainPane.setVisible(true);

        Stage stage = (Stage) playersDropDown.getScene().getWindow();
        stage.close();
    }

    @FXML
    void makeGame(ActionEvent event) {
        String output = playersDropDown.getSelectionModel().getSelectedItem();
        String[] split = output.split(" ");
        game.setAantalSpelers(Integer.parseInt(split[0]));

        output = sizeDropDown.getSelectionModel().getSelectedItem();
        split = output.split("x");
        game.setGrootteVeld(Integer.parseInt(split[0]));

        output = themeDropDown.getSelectionModel().getSelectedItem();
        game.setThema(output);

        try {
            implementation.makeGame(game);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void initMakeGameView() {

        String[] themas = {"Fruit","Pokemon"};

        for (int i = 1; i < AANTAL_SPELERS+1; i++) {
            playersDropDown.getItems().add(i*2 + " Spelers");
        }

        for(int i=1;i<GROOTTE_VELD+1;i++){
            int grootte = (int) Math.pow(2,i);
            sizeDropDown.getItems().add(grootte+"x"+grootte);
        }

        for(int i=0;i<AANTAL_THEMAS;i++){
            themeDropDown.getItems().add(themas[i]);
        }
    }

    public void setImplementation(IGameMethod implementation) {
        this.implementation = implementation;
    }

    public void setMainPane(AnchorPane mainPane) {
        this.mainPane = mainPane;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
