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

        System.out.println("veld maken:");
        //TODO Veld opmaken (misschien met nummers en 2d array)
        int aantalFiguren= (int) (Math.pow(game.getGrootteVeld(),2) / 2);
        game.maakVeld(database.getRandomAfbeeldingen(game.getThema(),aantalFiguren));
    }


    @Override
    public void releaseGame(Game game, User user) throws RemoteException {
        int index = -1;
        for (int i = 0; i < runningGames.size(); i++) {
            if (runningGames.get(i).getGameId() == game.getGameId()) {
                index = i;
            }
        }
        runningGames.get(index).removeUser(user);
        if (runningGames.get(index).getAantalSpelers() == 0) {
            if (index != -1) {
                runningGames.remove(index);
            }
        }

    }
}
