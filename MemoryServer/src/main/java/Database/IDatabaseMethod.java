package Database;

import Model.User;
import SpelLogica.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public interface IDatabaseMethod extends Remote{
    boolean checkCredentials(String name, String pas) throws RemoteException;
    int getID(String userName) throws RemoteException;
    List<Integer> getRandomAfbeeldingen(String thema, int aantal) throws RemoteException;
    byte[] getBackgroundImage(String thema) throws RemoteException;
    byte[] getImage(int id) throws RemoteException;
    void addGame(Game game) throws RemoteException;
    void logoutUser(User user) throws RemoteException;
    void updateUsersInGame(Game game) throws RemoteException;
    void removeUserInGame(Game game) throws RemoteException;
    void removeGame(int gameId, int serverId) throws RemoteException;
    User getInfo(User user) throws RemoteException;
    List<Game> getAllGames() throws  RemoteException;
    boolean createAccount(String name, String pas) throws RemoteException;
    String getToken(User user) throws RemoteException;
    boolean createSalt(byte[] salt, String name) throws RemoteException;
    byte[] getSalt(String name) throws RemoteException;
}
