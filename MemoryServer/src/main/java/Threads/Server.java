package Threads;

import DatabasePackage.Database;
import Game.GameMethod;
import Lobby.LobbyMethod;
import SpelLogica.Game;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Server {
    private int serverID;
    private Database database;
    private List<Game> runningGames;

    private final static int SERVERPOORT = 45062;

    public Server(int serverID) {
        this.serverID = serverID;
        System.out.println("Running Server " + serverID);
        runningGames = new ArrayList<>();
    }

    public void startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT + serverID*2);

            // create a new service named CounterService
            registry.rebind("LobbyService", new LobbyMethod(database,runningGames));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT + serverID*2 + 1);

            // create a new service named CounterService
            registry.rebind("GameService", new GameMethod(database,runningGames));
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
