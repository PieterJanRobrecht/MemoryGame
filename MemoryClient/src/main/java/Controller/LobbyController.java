package Controller;

import SpelLogica.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Set;

/**
 * Created by michi on 6/11/2016.
 */
public class LobbyController {


    @FXML

    private TableView<Game> overzichtGames;

    @FXML
    private TableColumn<Game, Integer> aantalSpelers;

    @FXML
    private TableColumn<Game, String> gameNaam;

    @FXML
    private TableColumn<Game, Integer> gewenstAantal;

    @FXML
    public void fillTable(){
        //aantalSpelers.setCellFactory(new PropertyValueFactory<SpelLogica.Game, String>("aantal spelers"));
        overzichtGames.setItems(getGames());
        overzichtGames.getColumns().addAll(aantalSpelers,gewenstAantal, gameNaam);
    }

    //Get all games
    public ObservableList<Game> getGames(){
        ObservableList<Game> games = FXCollections.observableArrayList();

        //later uit database halen!
        games.add(new Game(1,2,"thema SCRUBZ met Michiel!"));
        games.add(new Game(3,4,"thema Pokémon met PJ!"));

        return games;
    }

}
