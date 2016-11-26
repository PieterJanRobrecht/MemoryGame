package Game;

import Database.IDatabaseMethod;
import Model.User;
import SpelLogica.Game;
import SpelLogica.Move;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public class GameMethod extends UnicastRemoteObject implements IGameMethod {
    private IDatabaseMethod database;
    private List<Game> runningGames;

    public GameMethod(IDatabaseMethod database, List<Game> runningGames) throws RemoteException {
        this.database = database;
        this.runningGames = runningGames;
    }

    @Override
    public void makeGame(Game game) throws RemoteException {
        runningGames.add(game);
        database.addGame(game);

        System.out.println("veld maken:");
        int aantalFiguren = (int) (Math.pow(game.getGrootteVeld(), 2) / 2);
        game.maakVeld(database.getRandomAfbeeldingen(game.getThema(), aantalFiguren));
    }


    @Override
    public void releaseGame(Game game, User user) throws RemoteException {
        int index = -1;
        for (int i = 0; i < runningGames.size(); i++) {
            if (runningGames.get(i).getGameId() == game.getGameId()) {
                index = i;
            }
        }
        if(index!=-1) {
            runningGames.get(index).removeUser(user);
            if (runningGames.get(index).getAantalSpelers() == 0) {
                database.removeGame(runningGames.get(index).getGameId(), runningGames.get(index).getServerId());
                runningGames.remove(index);
            }
        }
    }

    @Override
    public void setNextBuzzyUser(int gameID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                game.setNextBuzzyID();
            }
        }
    }

    @Override
    public void resetMove(int gameID) throws RemoteException{
        for(Game game: runningGames){
            if (game.getGameId() == gameID){
                game.resetMove();
            }
        }

    }

    @Override
    public void removeUser(int gameID, User user) throws  RemoteException{
        for (int i = 0; i < runningGames.size(); i++) {
            if (gameID == runningGames.get(i).getGameId()) {
                System.out.println("users voor verwijderen op server "+runningGames.get(i).getAantalSpelers());
                runningGames.get(i).removeUser(user);
                System.out.println("users voor verwijderen op server "+runningGames.get(i).getAantalSpelers());

                database.RemoveUserInGame(runningGames.get(i));
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
    public Game getGame(Integer ID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == ID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public boolean doMove(Integer gameID, Integer userID, Move m) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                return game.doMove(userID, m);
            }
        }
        return false;
    }

    @Override
    public boolean voldoendeSpelers(Integer gameID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                return game.voldoendeSpelers();
            }
        }
        return false;
    }

    @Override
    public boolean isGameDone(int nGevonden, int gameID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                return nGevonden == (game.getGrootteVeld() * game.getGrootteVeld())/2;
            }
        }
        return false;
    }

    @Override
    public boolean addCardToMove(int col, int row, int gameID, int index) throws RemoteException{
        for(Game game: runningGames){
            if (game.getGameId() == gameID){
                return game.addCardToMove(col, row, index);
            }
        }
        return false;
    }

    @Override
    public Integer getbuzzyUserID(Integer gameID, Integer vorigeBuzzyUserID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                int nieuweBuzzyUserID = game.getBuzzyUserID();
                //while(nieuweBuzzyUserID == vorigeBuzzyUserID){//wachten
                    System.out.print(""); //vreemd, als je deze syso weg haalt werkt het niet...
                    nieuweBuzzyUserID = game.getBuzzyUserID();
                //}
                return game.getBuzzyUserID();
            }
        }
        return -2;
    }

    @Override
    public int getNieuwGevondeImages(List<Integer> reedsGevonden, Integer gameID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                List<Integer> ondertussenGevonden = game.getReedsGevondenImages();
                while (reedsGevonden.size() == ondertussenGevonden.size()) {//wachten
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("");
                    ondertussenGevonden = game.getReedsGevondenImages();

                }
                System.out.println("nieuwe afbeelding gevonden");
                return ondertussenGevonden.get(reedsGevonden.size());
            }
        }
        return -2;
    }

    @Override
    public int[] getCoordFromMove(int gameID, int index, int i) throws RemoteException, InterruptedException {
        for(Game game: runningGames){
            if (game.getGameId() == gameID){
                return game.getMove(index).getLastCoord(i);
            }
        }
        return null;
    }

    @Override
    public String getWinner(int gameID) throws RemoteException {
        for (Game game : runningGames) {
            if (game.getGameId() == gameID) {
                return game.getWinner();
            }
        }
        return null;
    }

    @Override
    public Move getMove(int gameID, int index) throws RemoteException{
        for(Game game: runningGames){
            if (game.getGameId() == gameID){
                try {
                    return game.getMove(index);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //methode voor de LobbyMethod
    public Game canMakeGame(User user) throws RemoteException {
        int gameID = -1;
        Game game = null;
        if (runningGames.size() != 20) {
            Collections.sort(runningGames, (o1, o2) -> o1.getGameId() - o2.getGameId());

            boolean found = false;
            for (int i = 0; i < runningGames.size(); i++) {
                if (runningGames.get(i).getGameId() != i) {
                    gameID = i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                gameID = runningGames.size();
            }

            game = new Game(gameID);
            game.addUser(user);
//           runningGames.add(game);
        }
        return game;
    }

    //methode voor de LobbyMethod
    public boolean addUserToGame(User thisUser, Game game, int serverID) throws RemoteException {
        if (game.getAantalSpelers() < game.getMaxAantalSpelers()) {
            if(game.getServerId() == serverID){
                for (int i = 0; i < runningGames.size(); i++) {
                    if (game.getGameId() == runningGames.get(i).getGameId()) {
                        runningGames.get(i).addUser(thisUser);
                        database.updateUsersInGame(runningGames.get(i));
                    }
                }
                return true;
            }
        }
        return false;
    }
}
