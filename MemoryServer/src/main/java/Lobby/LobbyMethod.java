package Lobby;

import Database.IDatabaseMethod;
import Model.User;
import Server.Server;
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
    private Server server;
    private int serverId;

    public LobbyMethod(IDatabaseMethod database, Server s, int serverId) throws RemoteException {
        this.database = database;
        userList = new ArrayList<>();
        this.server = s;
        this.serverId = serverId;
    }

    @Override
    public void removeUser(User user) throws RemoteException {
        int index = -1;
        database.getToken(user);
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getNaam().equals(user.getNaam())) {
                index = i;
            }
        }
        if (index != -1) {
            userList.remove(index);
        }
    }

    @Override
    public void addUser(User user) throws RemoteException {
        userList.add(user);
        database.getToken(user);
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
        List<Game> allRunningGames = database.getAllGames();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allRunningGames;
    }

    @Override
    public void logOutUser(User thisUser) throws RemoteException {
        database.getToken(thisUser);
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
        database.getToken(thisUser);
        return server.addUserToGame(thisUser, game);
    }

    @Override
    public Game canMakeGame(User user) throws RemoteException {
        database.getToken(user);
        return server.canMakeGame(user);
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
