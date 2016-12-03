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
    //TODO notification dat er iemand weg is gegaan
    private static List<Server> serverList;
    private static List<User> userList;
    private static List<Database> databaseList;

    private final static int NUMBER_OF_SERVERS = 3;
    private final static int NUMBER_OF_DATABASES = 3;

    public static void main(String[] args) {
        System.out.println("Main Start");

        userList = new ArrayList<>();

        databaseList = new ArrayList<Database>();
        for (int i = 0; i < NUMBER_OF_DATABASES; i++) {
            Database db = new Database("db"+i);
            databaseList.add(db);
        }
        legConnectieTussenDBes();

        serverList = new ArrayList<Server>();
        for (int i = 0; i < NUMBER_OF_SERVERS; i++) {
            Server s = new Server(i);
            serverList.add(s);
        }
        startDispatchingService();
        for (Server s: serverList){
            s.connectToDatabase();
        }

        for (Server s :
                serverList) {
            s.startRegistry();
        }
    }

    private static void legConnectieTussenDBes(){
        for(Database db1: databaseList) {
            for (Database db2 : databaseList) {
                if (db1 != db2) {
                    db1.addAndereDatabases(db2);
                }
            }
        }
    }


    private static void startDispatchingService() {
        Dispatcher dispatcher = new Dispatcher(serverList, databaseList, userList);
    }

    public static List<User> getUserList() {
        return userList;
    }
}
