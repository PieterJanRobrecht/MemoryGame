package Game;

import DatabasePackage.Database;
import Lobby.LobbyMethod;
import Model.User;
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
        runningGames.add(game);
    }

    @Override
    public void makeField(int gameId) throws RemoteException {
        //TODO Veld opmaken (misschien met nummers en 2d array)
    }

    @Override
    public void releaseGame(Game game, User user) throws RemoteException {
        game.removeUser(user);
        if (game.getAantalSpelers() == 0) {
            int index = -1;
            for (int i = 0; i < runningGames.size(); i++) {
                if (runningGames.get(i).getGameId() == game.getGameId()) {
                    index = i;
                }
            }
            if (index != -1) {
                runningGames.remove(index);
            }
        }

    }
}
