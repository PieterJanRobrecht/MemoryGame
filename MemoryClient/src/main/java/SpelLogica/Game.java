package SpelLogica;

/**
 * Created by michi on 6/11/2016.
 */
public class Game {
    private int aantalHuidigeGamers,gewenstAantalGamers,ID;
    private String gameNaam;

    public Game(int aantalHuidigeGamers, int gewenstAantalGamers, String gameNaam) {
        this.gewenstAantalGamers = gewenstAantalGamers;
        this.gameNaam = gameNaam;
        this.aantalHuidigeGamers = aantalHuidigeGamers;
    }



    public int getAantalHuidigeGamers() {
        return aantalHuidigeGamers;
    }

    public void setAantalHuidigeGamers(int aantalHuidigeGamers) {
        this.aantalHuidigeGamers = aantalHuidigeGamers;
    }

    public int getGewenstAantalGamers() {
        return gewenstAantalGamers;
    }

    public void setGewenstAantalGamers(int gewenstAantalGamers) {
        this.gewenstAantalGamers = gewenstAantalGamers;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGameNaam() {
        return gameNaam;
    }

    public void setGameNaam(String gameNaam) {
        this.gameNaam = gameNaam;
    }
}
