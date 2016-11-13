package Game;

import Model.User;
import SpelLogica.Game;
import SpelLogica.Move;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public interface IGameMethod extends Remote {
    void makeGame(Game game) throws RemoteException;
    void releaseGame(Game game, User user) throws RemoteException;
    byte[] getBackgroundImage(String thema) throws RemoteException;
    byte[] getImage(int id) throws RemoteException;
    Game getGame(Integer in) throws RemoteException;
    boolean doMove(Integer gameID,User u, Move m) throws RemoteException;

}
