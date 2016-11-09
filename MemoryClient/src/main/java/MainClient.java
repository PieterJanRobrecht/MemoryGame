
import Controller.LobbyController;
import Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Pieter-Jan on 30/10/2016.
 */
public class MainClient extends Application{

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
            LoginController loginController= loader.<LoginController>getController();
            assert (loginController != null);

            registry(loginController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registry(LoginController loginController){
        try{
            Registry myRegistry = LocateRegistry.getRegistry ("localhost", 45016);

            Registreer.IRegistreerMethod impl = (Registreer.IRegistreerMethod) myRegistry.lookup("RegistreerService");

            loginController.setImplementation(impl);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
