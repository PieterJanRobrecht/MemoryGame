package SpelLogica;

import Model.User;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by michi on 6/11/2016.
 */
public class Game implements Serializable {
    private Map<User, Integer> punten;
    private List<Integer> imageIDs;
    private String name;
    private int maxAantalSpelers;
    private int gameId;
    private String thema;
    //private int grootteVeld; //Dit is het aantal figuren in in rij of kolom
    private int[][] veld; //indien reeds opgelost dan veranderen we het id naar negatief
    private int aantalSpelers;

    public Game(int gameID) {
        this.gameId = gameID;
        punten = new HashMap<User, Integer>();
    }

    public void addUser(User u) {
        if (aantalSpelers < maxAantalSpelers || aantalSpelers==0) {
            punten.put(u,0);
            aantalSpelers++;
        }
    }

    public int[][] getVeld() {
        return veld;
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
        List spelers = new ArrayList<User>(punten.keySet());
        return spelers;
    }

    public List<Integer> getImageIDs() {
        return imageIDs;
    }

    public void setMaxAantalSpelers(int maxAantalSpelers) {
        this.maxAantalSpelers = maxAantalSpelers;
    }

    public void maakVeld(List<Integer> mogelijkeIDs){
        imageIDs = new ArrayList<Integer>();
        imageIDs.addAll(mogelijkeIDs);
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
        System.out.println("mogelijke IDs: "+imageIDs);
    }

    public void removeUser(User user) {
        punten.remove(user);
        aantalSpelers--;
    }

    public boolean doMove(User user, Move move){
        int idEersteKaart = veld[move.getCardX1()][move.getCardY1()];
        int idTweedeKaart = veld[move.getCardX2()][move.getCardY2()];
        if( (idEersteKaart == idTweedeKaart) && (idEersteKaart > -1) ){
            //punten.put(user, punten.get(user)+1);
            veld[move.getCardX1()][move.getCardY1()] = -idEersteKaart;
            veld[move.getCardX2()][move.getCardY2()] = -idTweedeKaart;
            return true;
        }
        return false;
    }
}
