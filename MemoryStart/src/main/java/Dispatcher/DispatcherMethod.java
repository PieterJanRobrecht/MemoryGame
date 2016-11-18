package Dispatcher;

import Main.Database;
import Model.User;
import Server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public class DispatcherMethod extends UnicastRemoteObject implements IDispatcherMethod{
    private List<Server> servers;
    private List<Database> databases;
    private List<User> users;

    private Map<Server, Database> serverToDatabase;
    private Map<User, Server> userToServer;

    public DispatcherMethod(List<Server> serverList, List<Database> databaseList, List<User> userList) throws RemoteException {
        servers = serverList;
        databases = databaseList;
        users = userList;
    }

    @Override
    public int getDatabaseId(Server server) throws RemoteException {
        //TODO schrijven van logica voor het verdelen van de databases
        return 0;
    }

    @Override
    public String getToken() throws RemoteException {
        String token = UUID.randomUUID().toString();
        return token;
    }

    @Override
    public int getServerId(User thisUser) throws RemoteException {
        //TODO schrijven van logica voor het verdelen van de users
        return 0;
    }
}
