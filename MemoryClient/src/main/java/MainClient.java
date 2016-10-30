import Controller.LoginController;
import UI.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Pieter-Jan on 30/10/2016.
 */
public class MainClient extends Application{
    private Stage stage;


    public static void main(String[] args) {
        System.out.println("Main Client");
        launch(args);
    }


    public void start(Stage primaryStage){
        try {
            //Laden van de fxml file waarin alle gui elementen zitten
            FXMLLoader loader = new FXMLLoader();
            Parent root = (Parent) loader.load(getClass().getClassLoader().getResource("Login.fxml").openStream());

            //Setten van enkele elementen van het hoofdscherm
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            //Ophalen van de controller horende bij de view klasse
            LoginView loginView = loader.<LoginView>getController() ;
            assert(loginView != null);

            LoginController controller = new LoginController();

            //Link tussen controller en view
            loginView.setLoginController(controller);
            controller.addObserver(loginView);
//            controller.setFileSmall();
//            lobbyView.initField();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
