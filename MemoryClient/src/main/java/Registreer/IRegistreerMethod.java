package Registreer;

import Model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public interface IRegistreerMethod extends Remote {
    User getUser(String userName, User user) throws RemoteException;
    boolean checkCredentials(String name, String pas) throws RemoteException;
    boolean createAccount(String name, String pas) throws RemoteException;
}
