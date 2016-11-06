package Registreer;

//import Model.User;
//import Threads.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public interface IRegistreerMethod extends Remote {
//    void addClient(User user) throws RemoteException;
//    Server getServer(int UserId) throws RemoteException;
    boolean checkCredentials(String name, String pas) throws RemoteException;
}
