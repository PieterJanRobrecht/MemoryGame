package Database;

import Model.User;
import SpelLogica.Game;
import Main.Database;
import javafx.scene.image.Image;
import sun.dc.pr.PRError;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public class DatabaseMethod extends UnicastRemoteObject implements IDatabaseMethod {
    private Connection databaseConnection;
    private Database database;

    public DatabaseMethod(Connection databaseConnection, Database db) throws RemoteException {
        this.databaseConnection = databaseConnection;
        database = db;
    }

    public boolean checkCredentials(String name, String pas) throws RemoteException {
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

    public int getID(String userName) throws RemoteException {
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

    public List<Integer> getRandomAfbeeldingen(String thema, int aantal) throws RemoteException {
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

    public byte[] getBackgroundImage(String thema) throws RemoteException {
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

    public byte[] getImage(int id) throws RemoteException {
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
        try {
            String query = "INSERT INTO GAME (SERVERID, NAME, GAMEID, MAXPLAYERS, CURRENTPLAYERS, SIZE, THEME) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, game.getServerId() + "");
            pst.setString(2, game.getName());
            pst.setString(3, game.getGameId() + "");
            pst.setString(4, game.getMaxAantalSpelers() + "");
            pst.setString(5, game.getAantalSpelers() + "");
            pst.setString(6, game.getGrootteVeld() + "");
            pst.setString(7, game.getThema());

            pst.executeUpdate();
            database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(game.getServerId() + "", game.getName(), game.getGameId() + "", game.getMaxAantalSpelers() + "", game.getAantalSpelers() + "", game.getGrootteVeld() + "", game.getThema())));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logoutUser(User user) throws RemoteException {
        try {
            String query = "UPDATE USER SET " +
                    "NWONGAMES =? ," +
                    "NLOSTGAMES=?," +
                    "TOKEN=?," +
                    "TIME=?" +
                    "WHERE ID =?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, user.getAantalGewonnen() + "");
            pst.setString(2, user.getAantalVerloren() + "");
            pst.setString(3, null);
            pst.setString(4, null);
            pst.setString(5, user.getId() + "");

            pst.executeUpdate();
            database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(user.getAantalGewonnen() + "", user.getAantalVerloren() + "", null, null, user.getId() + "")));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getInfo(User user) throws RemoteException {
        try {
            String query = "SELECT * FROM USER WHERE ID = ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, getID(user.getNaam()) + "");

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    user.setNaam(rs.getString("NAME"));
                    user.setId(rs.getInt("ID"));
                    user.setAantalGewonnen(rs.getInt("NWONGAMES"));
                    user.setAantalVerloren(rs.getInt("NLOSTGAMES"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void removeGame(int gameId, int serverId) throws RemoteException {
        try {
            String query = "DELETE FROM GAME WHERE GAMEID = ? AND SERVERID = ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, gameId + "");
            pst.setString(2, serverId + "");

            pst.executeUpdate();
            database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(gameId + "", serverId + "")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Game> getAllGames() throws RemoteException {
        List<Game> games = new ArrayList<>();
        try {
            String query = "SELECT * FROM GAME";
            PreparedStatement pst = databaseConnection.prepareStatement(query);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Game g = new Game(rs.getInt("GAMEID"));
                    g.setName(rs.getString("NAME"));
                    g.setMaxAantalSpelers(rs.getInt("MAXPLAYERS"));
                    g.setAantalSpelers(rs.getInt("CURRENTPLAYERS"));
                    g.setServerId(rs.getInt("SERVERID"));
                    g.setGrootteVeld(rs.getInt("SIZE"));
                    g.setThema(rs.getString("THEME"));
                    games.add(g);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    @Override
    public void updateUsersInGame(Game game) throws RemoteException {
        try {
            String query = "UPDATE GAME SET " +
                    "CURRENTPLAYERS =? " +
                    "WHERE GAMEID = ? " +
                    "AND SERVERID = ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, game.getAantalSpelers() + "");
            pst.setString(2, game.getGameId() + "");
            pst.setString(3, game.getServerId() + "");

            pst.executeUpdate();
            database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(game.getAantalSpelers() + "", game.getGameId() + "", game.getServerId() + "")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserInGame(Game game) throws RemoteException {
        try {
            String query = "UPDATE GAME SET " +
                    "CURRENTPLAYERS = ? " +
                    "WHERE GAMEID = ? " +
                    "AND SERVERID = ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, game.getAantalSpelers() + "");
            pst.setString(2, game.getGameId() + "");
            pst.setString(3, game.getServerId() + "");

            pst.executeUpdate();
            database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(game.getAantalSpelers() + "", game.getGameId() + "", game.getServerId() + "")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean createAccount(String name, String pas) throws RemoteException {
        if (!checkCredentials(name, pas)) {
            try {
                String query = "INSERT INTO USER (NAME, WW) VALUES (?,?)";
                PreparedStatement pst = databaseConnection.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, pas);

                pst.executeUpdate();
                database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(name, pas)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public String getToken(User user) throws RemoteException {
        try {
            String query = "SELECT * FROM USER WHERE ID = ?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, getID(user.getNaam()) + "");

            String d = null;
            String token = null;
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    d = rs.getString("TIME");
                    token = rs.getString("TOKEN");
                }
            }

            if (d != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                java.util.Date date = sdf.parse(d);
                java.util.Date now = new java.util.Date();
                long time = now.getTime() - date.getTime();
                if (token != null && time < TimeUnit.HOURS.toSeconds(24)) {
                    return token;
                } else {
                    token = UUID.randomUUID().toString();
                    setToken(token, user);
                    return token;
                }
            } else {
                token = UUID.randomUUID().toString();
                setToken(token, user);
                return token;
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setToken(String token, User user) {
        try {
            String query = "UPDATE USER SET " +
                    "TOKEN =? ," +
                    "TIME=? " +
                    "WHERE ID =?";
            PreparedStatement pst = databaseConnection.prepareStatement(query);
            pst.setString(1, token);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String date = sdf.format(new java.util.Date());

            pst.setString(2, date);
            pst.setString(3, getID(user.getNaam()) + "");

            pst.executeUpdate();
            database.broadCastQueryToAllDB(query, new ArrayList<String>(Arrays.asList(token, date, getID(user.getNaam()) + "")));

        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
