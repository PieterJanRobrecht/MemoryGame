package Main;

import Database.DatabaseMethod;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Database {
    private Connection databaseConnection;
    private String databaseNaam;
    private List<Database> andereDatabases;

    private int DATABASEPOORT;

    public Database(String dbNaam){
        databaseNaam = dbNaam;
        connectDatabase();
        createTables();
        startRepository();
        andereDatabases = new ArrayList<Database>();
    }

    public int getDATABASEPOORT() {
        return DATABASEPOORT;
    }

    public String getDatabaseNaam() {
        return databaseNaam;
    }

    public void addAndereDatabases(Database db){
        andereDatabases.add(db);
    }

    private void startRepository() {
        int dbId = Integer.parseInt(databaseNaam.substring(2));
        try {
            DATABASEPOORT =48745 + dbId;
            Registry registry = LocateRegistry.createRegistry(DATABASEPOORT);

            // create a new service named CounterService
            registry.rebind("DatabaseService", new DatabaseMethod(databaseConnection, this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Registry ready on database " + dbId);
    }

    private void connectDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            databaseConnection = DriverManager.getConnection("jdbc:sqlite:" + databaseNaam + ".db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database " + databaseNaam);
    }

    public void broadCastQueryToAllDB(String query, List<String> variabelen){
        for(Database db: andereDatabases){
            db.ontvangBroadCastQuery(query, variabelen);
        }
    }
    public void ontvangBroadCastQuery(String query, List<String> variabelen){
        try {
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            int i = 1;
            for(String var: variabelen){
                pst.setString(i, var);
                i++;
            }
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        Statement stmt = null;
        try {
            stmt = databaseConnection.createStatement();
            String sql = "CREATE TABLE USER " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME TEXT NOT NULL, " +
                    " WW TEXT NOT NULL," +
                    "NWONGAMES INTEGER NOT NULL DEFAULT 0," +
                    "NLOSTGAMES INTEGER NOT NULL DEFAULT 0," +
                    "NSTARTEDGAMES INTEGER NOT NULL DEFAULT 0)";

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        try{
            stmt = databaseConnection.createStatement();
            String sql = "CREATE TABLE GAME " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "SERVERID INTEGER NOT NULL," +
                    "GAMEID INTEGER NOT NULL," +
                    "NAME TEXT NOT NULL," +
                    "MAXPLAYERS INTEGER NOT NULL," +
                    "CURRENTPLAYERS INTEGER," +
                    "THEME TEXT," +
                    "SIZE INTEGER)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }

        try {
            stmt = databaseConnection.createStatement();
            String sql = "CREATE TABLE IMAGES " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " THEMA VARCHAR(40) NOT NULL, " +
                    " IMAGE BLOB)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
