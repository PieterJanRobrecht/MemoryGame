package SpelLogica;

import java.io.Serializable;

/**
 * Created by michi on 13/11/2016.
 */
public class Move implements Serializable {
    private int cardX1, cardY1, cardX2, cardY2, aatalKaarten = 0;

    public Move(){}

    public int getCardX1() {
        return cardX1;
    }

    public int getCardX2() {
        return cardX2;
    }

    public int getCardY1() {
        return cardY1;
    }

    public int getCardY2() {
        return cardY2;
    }

    public boolean addCardToMove(int cardX, int cardY){
        System.out.println("tovoegen van "+cardX+" "+cardY);
        switch (aatalKaarten) {
            case 0: {
                cardX1 = cardX;
                cardY1 = cardY;
                aatalKaarten++;
                return true;
            }
            case 1: {
                if (cardX1 == cardX && cardY1 == cardY){
                    return false;
                }
                cardX2 = cardX;
                cardY2 = cardY;
                aatalKaarten++;
                return true;
            }
        }
        return false;
    }

    public boolean isCompleet(){
        return aatalKaarten == 2;
    }
}
