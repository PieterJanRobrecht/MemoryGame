package DatabasePackage;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.xml.ws.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
                    " WW TEXT NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        stmt = null;
        try {
            stmt = databaseConnection.createStatement();
            String sql = "CREATE TABLE IMAGES " +
                    "(ID INT PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " THEMA VARCHAR(40) NOT NULL, " +
                    " IMAGE BLOB)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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
        System.out.println("LOOKUP USER " + userName);
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
        System.out.println("RETURN ID " + id);
        return id;
    }

    public List<Integer> getRandomAfbeeldingen(String thema, int aantal) {
        List<Integer> mogelijkeIDs = new ArrayList<Integer>();
        try {
            String query = "SELECT ID FROM IMAGES WHERE THEMA=?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, thema);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    mogelijkeIDs.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (mogelijkeIDs.size() > aantal) {
            mogelijkeIDs.remove(mogelijkeIDs.get(new Random().nextInt(mogelijkeIDs.size())));
        }
        return mogelijkeIDs;
    }

    public Image getBackgroundImage() {
        //TODO afbeelding uit database halen ipv. referentei (misschien achtergrond per thema?)
        try {
            // Grab the InputStream for the image.
            InputStream in = getClass().getResourceAsStream("MemoryGame/MemoryDatabase/src/resources/achterkant.jpg");
            // Then read it in.
            BufferedImage bf = ImageIO.read(in);

            //omzetten naar image javafx
            WritableImage wr = null;
            if (bf != null) {
                wr = new WritableImage(bf.getWidth(), bf.getHeight());
                PixelWriter pw = wr.getPixelWriter();
                for (int x = 0; x < bf.getWidth(); x++) {
                    for (int y = 0; y < bf.getHeight(); y++) {
                        pw.setArgb(x, y, bf.getRGB(x, y));
                    }
                }
                return wr;
            }

        } catch (IOException e) {
            System.out.println("The image was not loaded.");
        }
        return null;

    }
}
