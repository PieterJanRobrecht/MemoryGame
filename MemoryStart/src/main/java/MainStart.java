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
    private static List<Server> serverList;
    private List<User> userList;
    private static List<Database> databaseList;

    private final static int NUMBER_OF_SERVERS = 1;
    private final static int NUMBER_OF_DATABASES = 1;

    public static void main(String[] args) {
        System.out.println("Main Start");

        serverList = new ArrayList<Server>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            Server s = new Server(i);
            s.startRegistry();
            serverList.add(s);
        }

        //TODO Kijken voor methode om db connectie te sluiten
        databaseList = new ArrayList<Database>();
        for (int i = 0; i < NUMBER_OF_DATABASES; i++) {
            Database db = new Database("db1");
            databaseList.add(db);
        }

//        handOutDatabase();
        startDispatchingUsers();
    }

    private static void startDispatchingUsers() {
        System.out.println("Starting dispatcher");
        Dispatcher dispatcher = new Dispatcher(serverList);
    }
}
