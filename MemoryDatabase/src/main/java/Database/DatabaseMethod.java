package Database;

import SpelLogica.Game;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public class DatabaseMethod extends UnicastRemoteObject implements IDatabaseMethod {
    private Connection databaseConnection;

    public DatabaseMethod(Connection databaseConnection) throws RemoteException {
        this.databaseConnection = databaseConnection;
    }

    public boolean checkCredentials(String name, String pas) throws RemoteException{
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

    public int getID(String userName) throws RemoteException{
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

    public List<Integer> getRandomAfbeeldingen(String thema, int aantal) throws RemoteException{
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

    public byte[] getBackgroundImage(String thema) throws RemoteException{
        Image achtergrond;
        BufferedImage img;
        byte[] fileBytes = null;
        try {
            String query = "SELECT IMAGE FROM IMAGES WHERE THEMA=?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            String searchString = thema + "_Background";
            pst.setString(1, searchString);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    fileBytes = rs.getBytes(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    public byte[] getImage(int id) throws RemoteException{
        Image img;
        BufferedImage bufImg;
        byte[] fileBytes = null;
        try {
            String query = "SELECT IMAGE FROM IMAGES WHERE ID=?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    fileBytes = rs.getBytes(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    @Override
    public void addGame(Game game) throws RemoteException {
        try{
            String query = "INSERT INTO GAME (SERVERID, NAME, GAMEID) VALUES (?,?,?)";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, game.getServerId()+"");
            pst.setString(2, game.getName());
            pst.setString(3, game.getGameId()+"");

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
