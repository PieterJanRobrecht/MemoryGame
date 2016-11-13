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
    private HashMap<User, Integer> punten;
    private List<Integer> imageIDs;
    private String name;
    private int maxAantalSpelers;
    private int gameId;
    private String thema;
    //private int grootteVeld; //Dit is het aantal figuren in in rij of kolom
    private int[][] veld;
    private int aantalSpelers;
    private boolean uitgespeeld = false;

    public Game(int gameID) {
        this.gameId = gameID;
        punten = new HashMap<User, Integer>();
    }

    public void addUser(User u) {
        if (aantalSpelers < maxAantalSpelers || aantalSpelers==0) {
            punten.put(u,5);
            aantalSpelers++;
        }
        System.out.println("gamer toegevoegd: "+u.getNaam()+" en zijn beginpunten zijn "+punten.get(u));
    }

    public boolean isUitgespeeld() {
        return uitgespeeld;
    }

    public void setUitgespeeld(boolean uitgespeeld) {
        this.uitgespeeld = uitgespeeld;
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

    public User getUserFromGame(Integer userID){
        for ( User user : punten.keySet() ) {
            if (user.getId() == userID){
                return user;
            }
        }
        return null;
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
    }

    public void removeUser(User user) {
        punten.remove(user);
        aantalSpelers--;
    }

    public boolean doMove(Integer userID, Move move){
        int idEersteKaart = veld[move.getCardX1()][move.getCardY1()];
        int idTweedeKaart = veld[move.getCardX2()][move.getCardY2()];
        User user = getUserFromGame(userID);
        if( idEersteKaart == idTweedeKaart ){
            punten.put(user, punten.get(user)+1);
            System.out.println(" speler "+user.getNaam()+" heeft "+punten.get(user)+" punten.");
            return true;
        }
        return false;
    }

    public boolean voldoendeSpelers(){
        return aantalSpelers == maxAantalSpelers;
    }
}
