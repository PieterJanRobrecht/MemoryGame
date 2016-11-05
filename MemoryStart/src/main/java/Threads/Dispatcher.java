package Threads;

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
    private final static int SERVERPOORT = 45016;

    public Dispatcher(List<Server> serverList) {
        this.serverList = serverList;
        startRegistry();
    }

    private void startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT);

            // create a new service named CounterService
            registry.rebind("RegistreerService", new RegistreerMethod(serverList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Registry ready on dispatcher");
    }
}
