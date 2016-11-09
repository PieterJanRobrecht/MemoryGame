package DatabasePackage;

import java.sql.*;

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

    public boolean checkCredentials(String name, String pas) {
        try {
            String query = "SELECT (count(*) > 0) as found FROM USER WHERE \"NAME\" LIKE ? AND \"WW\" LIKE ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, pas);

            try (ResultSet rs = pst.executeQuery()) {
                // Only expecting a single result
                if (rs.next()) {
                    boolean found = rs.getBoolean(1); // "found" column
                    if (found) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getID(String userName) {
        int id = -1;
        System.out.println("LOOKUP USER " +userName);
        try {
            String query = "SELECT ID FROM USER WHERE \"NAME\" LIKE ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, userName);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("RETURN ID "+ id);
        return id;
    }
}
