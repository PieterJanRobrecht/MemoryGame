package Main;

import Database.DatabaseMethod;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Database {
    private Connection databaseConnection;
    private String databaseNaam;

    private static final int DATABASEPOORT = 48745;

    public Database(String dbNaam){
        databaseNaam = dbNaam;
        connectDatabase();
        createTables();
        startRepository();
    }

    private void startRepository() {
        int dbId = Integer.parseInt(databaseNaam.substring(2));
        try {
            Registry registry = LocateRegistry.createRegistry(DATABASEPOORT + dbId );

            // create a new service named CounterService
            registry.rebind("DatabaseService", new DatabaseMethod(databaseConnection));
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
                    "NAME TEXT NOT NULL)";
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
