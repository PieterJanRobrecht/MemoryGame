package Model;

import java.io.Serializable;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public class User implements Serializable{
    private int id;
    private String token;
    private String naam;
    private String wachtwoord;
    private int aantalGewonnen;
    private int aantalVerloren;

    public User() {
    }

    public User(String naam, String wachtwoord) {
        this.naam = naam;
        this.wachtwoord = wachtwoord;
        aantalGewonnen=0;
        aantalVerloren=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public void verhoogAantalWinnen() {
        aantalGewonnen++;
    }

    public void verhoogAantalVerliezen() {
        aantalVerloren++;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAantalGewonnen() {
        return aantalGewonnen;
    }

    public int getAantalVerloren() {
        return aantalVerloren;
    }

    public void setAantalGewonnen(int aantalGewonnen) {
        this.aantalGewonnen = aantalGewonnen;
    }

    public void setAantalVerloren(int aantalVerloren) {
        this.aantalVerloren = aantalVerloren;
    }
}
