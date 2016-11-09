package Lobby;

import DatabasePackage.Database;
import Model.User;
import SpelLogica.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class LobbyMethod extends UnicastRemoteObject implements ILobbyMethod {

    private final Database database;
    private List<User> userList;
    private List<Game> runningGames;


    public LobbyMethod(Database database, List<Game> runningGames) throws RemoteException {
        this.database = database;
        userList = new ArrayList<>();
        this.runningGames = runningGames;
    }

    @Override
    public void addUser(User user) throws RemoteException {
        userList.add(user);
    }

    @Override
    public List<String> getUserNames() throws RemoteException {
        List<String> namen = new ArrayList<>(userList.size());
        for (User u :
                userList) {
            namen.add(u.getNaam());
        }
        return namen;
    }

    @Override
    public Game canMakeGame(User user) throws RemoteException {
        int gameID = -1;
        Game game = null;
        if (runningGames.size() != 20) {
            Collections.sort(runningGames, (o1, o2) -> o1.getGameId() - o2.getGameId());

            boolean found = false;
            for (int i = 0; i < runningGames.size(); i++){
                if(runningGames.get(i).getGameId() != i){
                    gameID = i;
                    found = true;
                    break;
                }
            }

            if(!found){
                gameID = runningGames.size();
                found = true;
            }

            if(found){
                game = new Game(gameID);
                game.addUser(user);
                runningGames.add(game);
            }

        }
        return game;
    }
}
