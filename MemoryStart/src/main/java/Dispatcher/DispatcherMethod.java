package Dispatcher;

import Main.Database;
import Model.User;
import Server.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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

        serverToDatabase = new HashMap<Server, Database>();
        userToServer = new HashMap<User, Server>();
        //System.out.println("aantal databases is "+ databaseList.size());
    }

    @Override
    public int getDatabasePoort(Server server) throws RemoteException {
        if(serverToDatabase.get(server)!=null){ //indien de server reeds een db is toegewezen
            return serverToDatabase.get(server).getDATABASEPOORT();
        }
        Database minstBelastteDB = databases.get(0);
        int minBelasting = Collections.frequency(serverToDatabase.values(),databases.get(0));
        for(int i =1;i<databases.size();i++){ //db zoeken met minst aantal servers
            if (minBelasting > Collections.frequency(serverToDatabase.values(),databases.get(i))){
                minBelasting = Collections.frequency(serverToDatabase.values(),databases.get(i));
                minstBelastteDB = databases.get(i);
            }
        }
        serverToDatabase.put(server, minstBelastteDB);
        System.out.println("Server "+server.getServerID()+" is gekoppeld aan "+minstBelastteDB.getDatabaseNaam());

        return minstBelastteDB.getDATABASEPOORT();
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
