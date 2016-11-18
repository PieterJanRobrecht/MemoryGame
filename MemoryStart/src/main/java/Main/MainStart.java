package Main;

import Model.User;
import Dispatcher.Dispatcher;
import Server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 30/10/2016.
 */
public class MainStart {
    //TODO gridpane auto scale
    //TODO notification dat er iemand weg is gegaan
    //TODO als client stopt en die is aan beurt dan crasht de thread -> lobbycontroller 266
    //Wie moet met wie communiceren
    //TODO caching op de server en db
    private static List<Server> serverList;
    private static List<User> userList;
    private static List<Database> databaseList;

    private final static int NUMBER_OF_SERVERS = 2;
    private final static int NUMBER_OF_DATABASES = 3;

    public static void main(String[] args) {
        System.out.println("Main Start");

        userList = new ArrayList<>();

        startDispatchingService();

        //TODO Kijken voor methode om db connectie te sluiten
        databaseList = new ArrayList<Database>();
        for (int i = 0; i < NUMBER_OF_DATABASES; i++) {
            Database db = new Database("db"+i);
            databaseList.add(db);
        }

        serverList = new ArrayList<Server>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            Server s = new Server(i);
            serverList.add(s);
        }

        for (Server s :
                serverList) {
            s.startRegistry();
        }
    }

    private static void startDispatchingService() {
        Dispatcher dispatcher = new Dispatcher(serverList, databaseList, userList);
    }

    public static List<User> getUserList() {
        return userList;
    }
}
