package Database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Pieter-Jan on 18/11/2016.
 */
public interface IDatabaseMethod extends Remote{
    boolean checkCredentials(String name, String pas) throws RemoteException;
    int getID(String userName) throws RemoteException;
    List<Integer> getRandomAfbeeldingen(String thema, int aantal) throws RemoteException;
    byte[] getBackgroundImage(String thema) throws RemoteException;
    byte[] getImage(int id) throws RemoteException;
}
