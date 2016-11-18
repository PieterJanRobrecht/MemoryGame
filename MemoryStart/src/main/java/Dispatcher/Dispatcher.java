package Dispatcher;

import Main.Database;
import Model.User;
import Server.Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Dispatcher {
    private List<User> userList;
    private List<Server> serverList;
    private List<Database> databaseList;
    private final static int SERVERPOORT = 45016;

    public Dispatcher(List<Server> serverList, List<Database> databaseList, List<User> userList) {
        this.serverList = serverList;
        this.databaseList = databaseList;
        this.userList = userList;
        startRegistry();
    }

    private void startRegistry() {
        try {
            Registry registry = LocateRegistry.createRegistry(SERVERPOORT);

            // create a new service named CounterService
            registry.rebind("DispatcherService", new DispatcherMethod(serverList,databaseList,userList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Registry ready on dispatcher");
    }
}
