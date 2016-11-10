package Game;

import SpelLogica.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public interface IGameMethod extends Remote {
    void makeGame(Game game) throws RemoteException;
    void makeField(int gameId) throws RemoteException;
    void releaseGame(Game game) throws RemoteException;
}
