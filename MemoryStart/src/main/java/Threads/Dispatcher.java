package Threads;

import DatabasePackage.Database;
import Lobby.LobbyMethod;
import Registreer.RegistreerMethod;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Dispatcher {
    private List<Server> serverList;
    private Database database;
    private final static int SERVERPOORT = 45016;

    public Dispatcher(List<Server> serverList, Database database) {
        this.serverList = serverList;
        this.database = database;
        startRegistry();
    }

    private void startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT);

            // create a new service named CounterService
            registry.rebind("RegistreerService", new RegistreerMethod(serverList,database));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Registry ready on dispatcher");
    }
}
