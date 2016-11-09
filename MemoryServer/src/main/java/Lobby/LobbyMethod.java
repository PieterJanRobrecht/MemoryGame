package Lobby;

import DatabasePackage.Database;
import Model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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
}
