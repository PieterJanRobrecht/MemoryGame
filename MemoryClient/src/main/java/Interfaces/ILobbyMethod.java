package Interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by michi on 6/11/2016.
 */
public interface ILobbyMethod extends Remote{
    String toStringID() throws RemoteException;
}
