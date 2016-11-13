package Game;

import DatabasePackage.Database;
import Model.User;
import SpelLogica.Game;
import SpelLogica.Move;

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

    @Override
    public byte[] getBackgroundImage(String thema) throws RemoteException {
        return database.getBackgroundImage(thema);
    }

    @Override
    public byte[] getImage(int id) throws RemoteException {
        return database.getImage(id);
    }

    @Override
    public Game getGame(Integer ID) throws RemoteException{
        for(Game game: runningGames){
            if (game.getGameId() == ID){
                return game;
            }
        }
        return null;
    }

    @Override
    public boolean doMove(Integer gameID,Integer userID, Move m) throws RemoteException{
        for(Game game: runningGames){
            if (game.getGameId() == gameID){
                return game.doMove(userID, m);
            }
        }
        return false;
    }

    @Override
    public boolean voldoendeSpelers(Integer gameID) throws  RemoteException{
        for(Game game: runningGames){
            if (game.getGameId() == gameID){
                return game.voldoendeSpelers();
            }
        }
        return false;
    }



    }
