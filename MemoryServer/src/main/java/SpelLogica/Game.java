package SpelLogica;

import Model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by michi on 6/11/2016.
 */
public class Game implements Serializable{
    private List<User> gamers;
    private int nGewensteGamers;
    private int gameId;
    private String thema;
    private int grootteVeld;
    private int aantalSpelers;


//    public Game(Gamer initiator, int nGewensteGamers) {
//        gamers.add(initiator);
//        this.nGewensteGamers = nGewensteGamers;
//    }

    public Game(int gameID) {
        this.gameId = gameID;
        gamers = new ArrayList<User>();
    }

    public void addUser(User u){
        gamers.add(u);
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
}
