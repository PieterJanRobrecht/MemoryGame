package SpelLogica;

import Model.User;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by michi on 6/11/2016.
 */
public class Game implements Serializable {
    private List<User> gamers;
    private String name;
    private int maxAantalSpelers;
    private int gameId;
    private String thema;
    private int grootteVeld;
    private int aantalSpelers;

    public Game(int gameID) {
        this.gameId = gameID;
        gamers = new ArrayList<User>();
    }

    public void addUser(User u) {
        if (aantalSpelers < maxAantalSpelers || aantalSpelers==0) {
            gamers.add(u);
            aantalSpelers++;
        }
    }

    public int getGameId() {
        return gameId;
    }

    public int getGrootteVeld() {
        return grootteVeld;
    }

    public void setAantalSpelers(int aantalSpelers) {
        this.aantalSpelers = aantalSpelers;
    }

    public void setGrootteVeld(int grootteVeld) {
        this.grootteVeld = grootteVeld;
    }

    public void setThema(String thema) {
        this.thema = thema;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAantalSpelers() {
        return aantalSpelers;
    }

    public int getMaxAantalSpelers() {
        return maxAantalSpelers;
    }

    public void setMaxAantalSpelers(int maxAantalSpelers) {
        this.maxAantalSpelers = maxAantalSpelers;
    }
}
