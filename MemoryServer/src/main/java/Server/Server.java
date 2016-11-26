package Server;

import Database.IDatabaseMethod;
import Dispatcher.IDispatcherMethod;
import Game.GameMethod;
import Lobby.LobbyMethod;
import Model.User;
import Registreer.RegistreerMethod;
import SpelLogica.Game;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Server implements Serializable{
    private int serverID;
    private IDatabaseMethod database;
    private GameMethod gameMethod;
    private List<Game> runningGames;

    private final static int SERVERPOORT = 45062;
    private final static int DISPATCHERPOORT = 45016;
    private int DATABASEPOORT;

    public Server(int serverID) {
        this.serverID = serverID;
        System.out.println("Running Server " + serverID);
        runningGames = new ArrayList<>();

    }

    public void connectToDatabase() {
        IDispatcherMethod impl = connectToDispatcher();
        Registry myRegistry = null;
        try {
            DATABASEPOORT = impl.getDatabasePoort(this);
            myRegistry = LocateRegistry.getRegistry("localhost", DATABASEPOORT);

            database = (IDatabaseMethod) myRegistry.lookup("DatabaseService");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    private IDispatcherMethod connectToDispatcher() {
        Registry myRegistry = null;
        IDispatcherMethod impl = null;
        try {

            myRegistry = LocateRegistry.getRegistry("localhost", DISPATCHERPOORT);
            impl = (IDispatcherMethod) myRegistry.lookup("DispatcherService");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return impl;
    }

    public void startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT + serverID * 3);

            // create a new service named CounterService
            registry.rebind("LobbyService", new LobbyMethod(database, this, serverID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT + serverID * 3 + 1);

            // create a new service named CounterService
            gameMethod = new GameMethod(database, runningGames);
            registry.rebind("GameService", gameMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT + serverID * 3 + 2);

            // create a new service named CounterService
            registry.rebind("RegistreerService", new RegistreerMethod(database));
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

    public Game canMakeGame(User user) throws RemoteException {
        return gameMethod.canMakeGame(user);
    }

    public boolean addUserToGame(User thisUser, Game game) throws RemoteException {
        return gameMethod.addUserToGame(thisUser,game, serverID);
    }
}
