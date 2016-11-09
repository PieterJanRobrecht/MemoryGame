package Game;

import DatabasePackage.Database;
import SpelLogica.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public class GameMethod extends UnicastRemoteObject implements IGameMethod {
    private Database database;
    private List<Game> runningGames;

    public GameMethod(Database database, List<Game> runningGames) throws RemoteException {
        this.database = database;
        this.runningGames = runningGames;
    }

    @Override
    public void makeGame(Game game) throws RemoteException {
        //Maak spel maar niet meer toevoegen aan lijst(heeft die namelijk al)
    }
}
