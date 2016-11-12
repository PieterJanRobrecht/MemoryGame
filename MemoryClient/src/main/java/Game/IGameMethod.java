package Game;

import Model.User;
import SpelLogica.Game;
import javafx.scene.image.Image;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public interface IGameMethod extends Remote{
    void makeGame(Game game) throws RemoteException;
    void releaseGame(Game game, User user) throws RemoteException;
    Image getBackgroundImage() throws RemoteException;


}
