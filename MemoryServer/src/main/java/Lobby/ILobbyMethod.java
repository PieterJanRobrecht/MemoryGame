package Lobby;

import Model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Pieter-Jan on 05/11/2016.
 */
public interface ILobbyMethod extends Remote {
    void addUser(User user) throws RemoteException;
    List<String> getUserNames()throws RemoteException;
}
