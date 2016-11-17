package Main;

import DatabasePackage.Database;
import Model.User;
import Threads.Dispatcher;
import Threads.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 30/10/2016.
 */
public class MainStart {
    //TODO gridpane auto scale
    //TODO notification dat er iemand weg is gegaan
    //TODO datbase moet met RMI
    //TODO als client stopt en die is aan beurt dan crasht de thread -> lobbycontroller 266
    //Wie moet met wie communiceren
    //TODO inloggen op server niet op dispatcher
    private static List<Server> serverList;
    private static List<User> userList;
    private static List<Database> databaseList;

    private final static int NUMBER_OF_SERVERS = 1;
    private final static int NUMBER_OF_DATABASES = 1;

    public static void main(String[] args) {
        System.out.println("Main Start");

        userList = new ArrayList<>();

        serverList = new ArrayList<Server>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            Server s = new Server(i);
            serverList.add(s);
        }

        //TODO Kijken voor methode om db connectie te sluiten
        databaseList = new ArrayList<Database>();
        for (int i = 0; i < NUMBER_OF_DATABASES; i++) {
            Database db = new Database("db"+i);
            databaseList.add(db);
        }

        handOutDatabases();
        startDispatchingUsers();

        for (Server s :
                serverList) {
            s.startRegistry();
        }
    }

    private static void handOutDatabases() {
        serverList.get(0).setDatabase(databaseList.get(0));
    }

    private static void startDispatchingUsers() {
        System.out.println("Starting dispatcher");
        Dispatcher dispatcher = new Dispatcher(serverList,databaseList.get(0));
    }

    public static List<User> getUserList() {
        return userList;
    }
}
