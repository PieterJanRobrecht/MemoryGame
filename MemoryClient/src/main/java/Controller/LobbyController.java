package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LobbyController {

    @FXML
    private TableColumn<?, ?> numbersOfPlayersColumn;

    @FXML
    private TableColumn<?, ?> joinColumn;

    @FXML
    private TextArea globalChat;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextArea lobbyChat;

    @FXML
    private TextArea playerList;

    @FXML
    private TableColumn<?, ?> maxPlayersColumn;

    @FXML
    private TableColumn<?, ?> spectateColumn;

    @FXML
    private TextField sendTextField;

    @FXML
    void sendMessage(ActionEvent event) {

    }

    @FXML
    void logOut(ActionEvent event) {

    }

    @FXML
    void makeNewGame(ActionEvent event) {

    }

    @FXML
    void about(ActionEvent event) {

    }

}
