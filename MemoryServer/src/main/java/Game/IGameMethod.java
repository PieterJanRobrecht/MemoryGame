package Game;

import Model.User;
import SpelLogica.Game;
import SpelLogica.Move;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Pieter-Jan on 09/11/2016.
 */
public interface IGameMethod extends Remote {
    void makeGame(Game game) throws RemoteException;
    void releaseGame(Game game, User user) throws RemoteException;
    void setNextBuzzyUser(int gameID) throws  RemoteException;
    void resetMove(int gameID) throws RemoteException;
    byte[] getBackgroundImage(String thema) throws RemoteException;
    byte[] getImage(int id) throws RemoteException;
    Game getGame(Integer in) throws RemoteException;
    boolean doMove(Integer gameID,Integer userID, Move m) throws RemoteException;
    boolean voldoendeSpelers(Integer gameID) throws  RemoteException;
    boolean isGameDone(int nGevonden,int gameID) throws RemoteException;
    boolean addCardToMove(int col, int row, int gameID, int index) throws RemoteException;
    Integer getbuzzyUserID(Integer gameID, Integer vorigeBuzzyUserID) throws RemoteException;
    int getNieuwGevondeImages(List<Integer> reedsGevonden, Integer gameID) throws RemoteException;
    int[] getCoordFromMove(int gameID, int index, int i) throws RemoteException, InterruptedException;
    String getWinner(int gameID) throws RemoteException;
    Move getMove(int gameID, int index) throws RemoteException;




}
