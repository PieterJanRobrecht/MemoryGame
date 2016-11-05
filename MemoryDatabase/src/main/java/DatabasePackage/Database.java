package DatabasePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class Database {
    private Connection databaseConnection;
    private String databaseNaam;

    public Database(String dbNaam) {
        databaseNaam = dbNaam;
        connectDatabase();
        createTables();
    }

    private void connectDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            databaseConnection = DriverManager.getConnection("jdbc:sqlite:"+databaseNaam+".db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database "+databaseNaam);
    }

    private void createTables(){
        Statement stmt = null;
        try {
            stmt = databaseConnection.createStatement();
            String sql = "CREATE TABLE USER " +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME TEXT NOT NULL, " +
                    " WW TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}
