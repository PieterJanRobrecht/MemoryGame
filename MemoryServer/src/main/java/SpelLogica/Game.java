package SpelLogica;

import java.util.Set;

/**
 * Created by michi on 6/11/2016.
 */
public class Game {
    private Set<Gamer> gamers;
    private int nGewensteGamers;


    public Game(Gamer initiator, int nGewensteGamers) {
        gamers.add(initiator);
        this.nGewensteGamers = nGewensteGamers;
    }

    public void addGamer(Gamer g){
        gamers.add(g);
    }
}
