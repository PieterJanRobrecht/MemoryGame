package SpelLogica;

import Model.User;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    //private int grootteVeld; //Dit is het aantal figuren in in rij of kolom
    private int[][] veld;
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

    public String getThema() {
        return thema;
    }

    public int getGameId() {
        return gameId;
    }

    public int getGrootteVeld() {
        return veld[0].length;
    }

    public void setAantalSpelers(int aantalSpelers) {
        this.aantalSpelers = aantalSpelers;
    }

    public void setGrootteVeld(int grootteVeld) {
        this.veld = new int[grootteVeld][grootteVeld];
        //this.grootteVeld = grootteVeld;
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

    public List<User> getGamers() {
        return gamers;
    }

    public void setMaxAantalSpelers(int maxAantalSpelers) {
        this.maxAantalSpelers = maxAantalSpelers;
    }

    public void maakVeld(List<Integer> mogelijkeIDs){
        mogelijkeIDs.addAll(mogelijkeIDs); //iedere figuur zit twee keer in de lijst
        int id, index, lengte = veld[0].length;
        for(int i=0;i<lengte;i++){
            for(int j=0;j<lengte;j++){
                index = new Random().nextInt(mogelijkeIDs.size());
                id=mogelijkeIDs.get(index);
                veld[i][j] = id;
                mogelijkeIDs.remove(index);
                System.out.print("  "+id);
            }
            System.out.println();
        }
    }

    public void removeUser(User user) {
        int index =0;
        for(int i=0;i<gamers.size();i++){
            if(gamers.get(i).getId()==user.getId()){
                index=i;
            }
        }
        gamers.remove(index);
        aantalSpelers--;
    }
}
