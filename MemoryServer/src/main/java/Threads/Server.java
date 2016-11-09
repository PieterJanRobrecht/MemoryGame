package Threads;

import DatabasePackage.Database;
import Lobby.LobbyMethod;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Server {
    private int serverID;
    private Database database;

    private final static int SERVERPOORT = 45062;

    public Server(int serverID) {
        this.serverID = serverID;
        System.out.println("Running Server " + serverID);
    }

    public void startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT + serverID);

            // create a new service named CounterService
            registry.rebind("LobbyService", new LobbyMethod(database));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Registry ready on server " + serverID);
    }

    public int getServerID() {
        return serverID;
    }

    public static int getSERVERPOORT() {
        return SERVERPOORT;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }
}
