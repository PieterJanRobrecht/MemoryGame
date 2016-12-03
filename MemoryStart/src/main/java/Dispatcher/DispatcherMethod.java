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
public class DispatcherMethod extends UnicastRemoteObject implements IDispatcherMethod {
    private static final int MAX_AANTAL_SPELERS = 2;
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
        if (serverToDatabase.get(server) != null) { //indien de server reeds een db is toegewezen
            return serverToDatabase.get(server).getDATABASEPOORT();
        }
        Database minstBelastteDB = databases.get(0);
        int minBelasting = Collections.frequency(serverToDatabase.values(), databases.get(0));
        for (int i = 1; i < databases.size(); i++) { //db zoeken met minst aantal servers
            if (minBelasting > Collections.frequency(serverToDatabase.values(), databases.get(i))) {
                minBelasting = Collections.frequency(serverToDatabase.values(), databases.get(i));
                minstBelastteDB = databases.get(i);
            }
        }
        serverToDatabase.put(server, minstBelastteDB);
        System.out.println("Server " + server.getServerID() + " is gekoppeld aan " + minstBelastteDB.getDatabaseNaam());

        return minstBelastteDB.getDATABASEPOORT();
    }

    @Override
    public int getServerId(User thisUser) throws RemoteException {
        //TODO eventueel ook een methode toevoegen die amper gebruikte servers verwijderd
        boolean nogServersOver = true;
        int loper = 0;
        while (nogServersOver) {
            if (MAX_AANTAL_SPELERS > Collections.frequency(userToServer.values(), servers.get(loper))) {
                User hulp = null;
                for (Map.Entry pair : userToServer.entrySet()) {
                    User u = (User) pair.getKey();
                    if (u.getId() == thisUser.getId() && u.getToken() == null) {
                        hulp = u;
                    }
                }
                if (hulp == null) {
                    userToServer.put(thisUser, servers.get(loper));
                } else {
                    userToServer.put(hulp, servers.get(loper));
                }
                return servers.get(loper).getServerID();
            }

            loper++;
            nogServersOver = (servers.size() > loper);
        }

        //starten nieuwe server
        Server s = new Server(loper);
        s.connectToDatabase();
        s.startRegistry();

        servers.add(s);
        userToServer.put(thisUser, servers.get(loper));

        return loper;
    }

    @Override
    public void changeServerUser(User user, int serverId) throws RemoteException {
        for (Object o : userToServer.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            User u = (User) pair.getKey();
            if (u.getId() == user.getId()) {
                pair.setValue(servers.get(serverId));
            }
        }
    }

    @Override
    public void updateUserServer(User user, int serverId) throws RemoteException {
        for (Map.Entry pair : userToServer.entrySet()) {
            User u = (User) pair.getKey();
            if (u.getToken() == null) {
                userToServer.remove(u);
                break;
            }
        }
        userToServer.put(user, servers.get(serverId));
    }
}
