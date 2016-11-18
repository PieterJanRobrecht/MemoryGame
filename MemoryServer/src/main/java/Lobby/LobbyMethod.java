package Lobby;

import Database.IDatabaseMethod;
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

    private final IDatabaseMethod database;
    private List<User> userList;
    private List<Game> runningGames;
    private int serverId;

    public LobbyMethod(IDatabaseMethod database, List<Game> runningGames, int serverId) throws RemoteException {
        this.database = database;
        userList = new ArrayList<>();
        this.runningGames = runningGames;
        this.serverId = serverId;
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

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return namen;
    }

    @Override
    public List<Game> getRunningGames() throws RemoteException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return runningGames;
    }

    @Override
    public void logOutUser(User thisUser) throws RemoteException {
        int index = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getNaam().equals(thisUser.getNaam())) {
                index = i;
            }
        }
        if (index != -1) {
            database.logoutUser(thisUser);
            userList.remove(index);
        }
        System.out.println("Log uit gebruiker " + thisUser.getNaam());
    }

    @Override
    public boolean addUserToGame(User thisUser, Game game) throws RemoteException {
        if (game.getAantalSpelers() < game.getMaxAantalSpelers()) {
            if(game.getServerId() == serverId){
                for (int i = 0; i < runningGames.size(); i++) {
                    if (game.getGameId() == runningGames.get(i).getGameId()) {
                        runningGames.get(i).addUser(thisUser);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
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

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
