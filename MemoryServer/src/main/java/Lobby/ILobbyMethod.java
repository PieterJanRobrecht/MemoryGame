package Lobby;

import Model.User;
import SpelLogica.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public interface ILobbyMethod extends Remote {
    void removeUser(User user) throws RemoteException;
    void addUser(User user) throws RemoteException;
    List<String> getUserNames()throws RemoteException;
    Game canMakeGame(User user) throws RemoteException;
    List<Game> getRunningGames() throws RemoteException;
    void logOutUser(User thisUser) throws RemoteException;
    boolean addUserToGame(User thisUser, Game game) throws RemoteException;
}
