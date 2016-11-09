package Lobby;

import DatabasePackage.Database;
import Model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class LobbyMethod extends UnicastRemoteObject implements ILobbyMethod {

    private final Database database;
    private List<User> userList;


    public LobbyMethod(Database database) throws RemoteException {
        this.database = database;
        userList = new ArrayList<>();
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
}
